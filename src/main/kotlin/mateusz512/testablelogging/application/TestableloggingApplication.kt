package mateusz512.testablelogging.application

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["mateusz512.testablelogging.application"])
class TestableloggingApplication

fun main(args: Array<String>) {
	runApplication<TestableloggingApplication>(*args)
}
