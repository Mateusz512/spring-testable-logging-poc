package mateusz512.testablelogging.application

import mateusz512.testablelogging.domain.FooService
import mateusz512.testablelogging.domain.Events
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Config {
    @Bean
    fun fooService(events: Events): FooService = FooService(events)
}