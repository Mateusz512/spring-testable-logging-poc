package mateusz512.testablelogging.application

import mateusz512.testablelogging.domain.Event
import org.springframework.context.ApplicationEvent
import java.time.Clock

class DomainEvent(val event: Event, clock: Clock) : ApplicationEvent(event, clock)

fun interface DomainEventConsumer {
    operator fun invoke(domainEvent: DomainEvent)
}