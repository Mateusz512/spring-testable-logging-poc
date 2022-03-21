package mateusz512.testablelogging.application

import mateusz512.testablelogging.domain.FooService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Config {
    @Bean
    fun fooService(): FooService = FooService()
}