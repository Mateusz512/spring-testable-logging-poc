package mateusz512.testablelogging.application

import mateusz512.testablelogging.domain.FooInvoked
import mateusz512.testablelogging.domain.FooService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.test.context.event.ApplicationEvents
import org.springframework.test.context.event.RecordApplicationEvents
import strikt.api.expectThat
import strikt.assertions.containsExactly
import strikt.assertions.isEqualTo
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import kotlin.streams.toList


@TestConfiguration
class TestConfig {
    @Bean
    fun clock(): Clock = Clock.fixed(Instant.EPOCH, ZoneId.of("UTC"))
}

@SpringBootTest
@Import(TestConfig::class)
@RecordApplicationEvents
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class TestableloggingApplicationTests {

    @Autowired
    lateinit var fooService: FooService

    @Autowired
    lateinit var events: ApplicationEvents

    @Test
    fun contextLoads() {
        expectThat(
            fooService.bar()
        ).isEqualTo("how-much-is-the-fish")
    }

    @Test
    fun `should log something`() {
        fooService.bar()
        expectThat(
            domainEvents
        ).containsExactly(FooInvoked)
    }

    private val domainEvents
        get() = events.stream(SpringEvents.DomainEvent::class.java).map { it.event }.toList()

}