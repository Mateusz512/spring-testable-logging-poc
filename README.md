# POC for testable logging in Spring

## The concept

Testing logging... Insane, right?

...or is it?

Logging is part of the code, right? And code **should** be testable.

How do you make sure, that after some major refactoring, you would still be able to see the things you want to see in
the logs? And don't even think of checking it manually...

The answer is: by testing it!

```kotlin
val potion = potionBrewer.doMagic(specialIngredients)

expectThat(potion).isMagical()
expectThat(logs).single().isEqualTo(PotionCreated(specialIngredients))
```

This snippet suggests the following:

1. Logs should be managed by something injectable/centralized.
2. Logs themselves should not be bound to where they would end up. We don't want to assert that some string was printed
   to some printer. They should share some common abstraction, and at the very basic level, should just be data class
   objects. Moreover, logs should be structurized - it's easier to assert
   ```kotlin
   expectThat(aLog).isEqualTo(PotionCreated(specialIngredients))
   ```
   than
   ```kotlin
   expectThat(aLog).isEqualTo("Potion created (ingredients = ...)")
   ```
   Structurized stuff is also easier to print to JSON (all cool kids do that) for easier indexing, traceability and
   overall readability.
3. Last but not least: logs should contain some metadata. Timestamps are crucial. In distributed systems, you should
   also see, which pod/node produced this log.

## The API

The API is as simple as it can be. Based on the `Event` in [http4k](https://github.com/http4k/http4k).

Log is an event.

```kotlin
interface Event
```

Events can be consumed.

```kotlin
fun interface Events {
    operator fun invoke(event: Event)
}
```

Why the name "Events"? Think of the usage:

```kotlin
events(someEvent)
```

## The Spring bits

Spring has a concept
of [ApplicationEvent](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/context/ApplicationEvent.html)
. It can be passed around in Spring context and
even [captured in tests](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/test/context/event/RecordApplicationEvents.html#:~:text=%40RecordApplicationEvents%20is%20a%20class%2Dlevel,ApplicationEvents%20API%20within%20your%20tests.)!
It even has a concept of passing a `Clock` to it! (FOR THE LOVE OF GOD. `Instant.now()` without a `Clock` should be
prohibited...). But that's not very convenient to use every time you want to make an event. Also: the `source` is
extremely generic (it's an `Object), So we should wrap it somehow.

```kotlin
class DomainEvent(val event: Event, clock: Clock) : ApplicationEvent(event, clock)
```

Ok, but what about the clock. We don't want it in all our beans.

Wrapping our `Event`s should happen in one place.

```kotlin
@Component
class SpringEvents(private val clock: Clock) : Events, ApplicationEventPublisherAware {

    private lateinit var applicationEventPublisher: ApplicationEventPublisher

    override fun setApplicationEventPublisher(applicationEventPublisher: ApplicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher
    }

    override fun invoke(event: Event) {
        applicationEventPublisher.publishEvent(DomainEvent(event, clock))
    }

}
```

Nice. We have clock in one place. Now we can inject this bean to wherever we want to publish some events, without
dirtying our domain logic with Spring dependencies.

```kotlin
class PotionBrewer(private val events: Events) {

    fun doMagic(specialIngredients: SpecialIngredients) =
        specialIngredients.toPotion().also { events(PotionCreated(specialIngredients)) }

}

object PotionCreated(specialIngredients: SpecialIngredients) : Event
```

And how testable is that!

```kotlin
@TestConfiguration
class TestConfig {
    @Bean
    fun clock(): Clock = Clock.fixed(Instant.EPOCH, ZoneId.of("UTC"))
}

@SpringBootTest
@Import(TestConfig::class)
@RecordApplicationEvents
class TestableloggingApplicationTests {

    @Autowired
    lateinit var potionBrewer: PotionBrewer

    @Autowired
    lateinit var events: ApplicationEvents

    private val logs
        get() = events.stream(DomainEvent::class.java).map { it.event }.toList()

    @Test
    fun `given valid special ingredients, upon doing magic, should brew a magical potion`() {
        val specialIngredients = SpecialIngredients.fromSpellBook()
        val potion = potionBrewer.doMagic(specialIngredients)

        expectThat(potion).isMagical()
        expectThat(logs).containsExactly(PotionCreated(specialIngredients))
    }

}
```