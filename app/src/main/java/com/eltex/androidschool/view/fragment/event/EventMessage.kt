package com.eltex.androidschool.view.fragment.event

import arrow.core.Either
import com.eltex.androidschool.domain.model.Event
import com.eltex.androidschool.view.model.EventUi
import com.eltex.androidschool.view.model.EventWithError

sealed interface EventMessage {
    data object LoadNextPage : EventMessage
    data object Refresh : EventMessage
    data class Like(val event: EventUi) : EventMessage
    data class Delete(val event: EventUi) : EventMessage
    data class Participant(val event: EventUi) : EventMessage
    data object HandleError : EventMessage

    data class DeleteError(val error: EventWithError) : EventMessage
    data class LikeResult(val result: Either<EventWithError, Event>) : EventMessage
    data class ParticipantResult(val result: Either<EventWithError, Event>) : EventMessage
    data class InitialLoaded(val result: Either<Throwable, List<Event>>) : EventMessage
    data class NextPageLoaded(val result: Either<Exception, List<Event>>) : EventMessage
}