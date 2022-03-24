package mateusz512.testablelogging.application

import mateusz512.testablelogging.domain.Event
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.time.Clock
import java.time.Instant
import java.time.ZoneId

internal class PrinterTest {
    private val clock = Clock.fixed(Instant.EPOCH, ZoneId.of("UTC"))

    @Test
    fun `should serialize domain events to printstream`() {
        val stream = ByteArrayOutputStream()
        val printer = Printer(PrintStream(stream))

        data class SomeEvent(val foo: String, val bar: Int) : Event

        printer(DomainEvent(SomeEvent("baz", 1), clock))

        expectThat(
            stream.toString().trim()
        ).isEqualTo("PrintableWrapper(event=SomeEvent(foo=baz, bar=1), timestamp=1970-01-01T00:00:00Z)") //TODO: convert to JSON
    }
}