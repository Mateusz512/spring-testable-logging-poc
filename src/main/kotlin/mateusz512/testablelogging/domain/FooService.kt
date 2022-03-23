package mateusz512.testablelogging.domain

class FooService(private val events: Events) {

    fun bar() = "how-much-is-the-fish".also { events(FooInvoked) }

}

object FooInvoked : Event