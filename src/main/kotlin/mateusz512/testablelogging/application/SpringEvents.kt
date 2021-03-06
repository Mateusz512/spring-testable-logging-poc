package mateusz512.testablelogging.application

import mateusz512.testablelogging.domain.Event
import mateusz512.testablelogging.domain.Events
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.ApplicationEventPublisherAware
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import java.time.Clock

@Component
class SpringEvents(private val clock: Clock) : Events, ApplicationEventPublisherAware {

    private val printer: DomainEventConsumer = Printer(System.out)

    private lateinit var applicationEventPublisher: ApplicationEventPublisher

    override fun setApplicationEventPublisher(applicationEventPublisher: ApplicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher
    }

    override fun invoke(event: Event) {
        applicationEventPublisher.publishEvent(DomainEvent(event, clock))
    }

    @EventListener
    fun consumeDomainEvent(domainEvent: DomainEvent) {
        printer(domainEvent)
    }

}