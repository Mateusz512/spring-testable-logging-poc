package mateusz512.testablelogging.application

import mateusz512.testablelogging.domain.FooService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import strikt.api.expectThat
import strikt.assertions.isEqualTo

@SpringBootTest
class TestableloggingApplicationTests {

	@Autowired
	lateinit var fooService: FooService

	@Test
	fun contextLoads() {
		expectThat(
			fooService.bar()
		).isEqualTo("how-much-is-the-fish")
	}

}