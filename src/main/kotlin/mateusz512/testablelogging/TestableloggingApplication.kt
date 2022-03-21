package mateusz512.testablelogging

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TestableloggingApplication

fun main(args: Array<String>) {
	runApplication<TestableloggingApplication>(*args)
}
