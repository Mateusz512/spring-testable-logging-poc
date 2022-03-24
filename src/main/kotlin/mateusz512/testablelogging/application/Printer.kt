package mateusz512.testablelogging.application

import mateusz512.testablelogging.domain.Event
import java.io.PrintStream
import java.time.Instant

class Printer(private val printStream: PrintStream) : DomainEventConsumer {


    override fun invoke(domainEvent: DomainEvent) {
        val printableWrapper = PrintableWrapper(domainEvent.event, Instant.ofEpochMilli(domainEvent.timestamp))
        printStream.println(printableWrapper.toString())
    }

    data class PrintableWrapper(val event: Event, val timestamp: Instant)
}