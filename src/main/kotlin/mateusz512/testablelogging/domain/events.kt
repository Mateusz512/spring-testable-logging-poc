package mateusz512.testablelogging.domain


interface Event

fun interface Events {
    operator fun invoke(event: Event)
}
