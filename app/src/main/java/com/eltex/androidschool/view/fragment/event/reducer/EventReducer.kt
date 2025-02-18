package com.eltex.androidschool.view.fragment.event.reducer

import arrow.core.Either
import com.eltex.androidschool.domain.mapper.Mapper
import com.eltex.androidschool.domain.model.Event
import com.eltex.androidschool.mvi.Reducer
import com.eltex.androidschool.mvi.ReducerResult
import com.eltex.androidschool.view.fragment.event.EventEffect
import com.eltex.androidschool.view.fragment.event.EventMessage
import com.eltex.androidschool.view.fragment.event.EventState
import com.eltex.androidschool.view.fragment.event.EventStatus
import com.eltex.androidschool.view.fragment.post.reducer.PostReducer
import com.eltex.androidschool.view.model.EventUi
import javax.inject.Inject

/**
 * `EventReducer` is a crucial component in managing the state of events within the application.
 * It acts as a pure function that takes the current `EventState` and an incoming [EventMessage],
 * and then produces a new [EventState] along with any associated side-effects ([EventEffect]).
 *
 * The [EventReducer] is responsible for handling various actions related to events, such as:
 * - Deleting events
 * - Managing user participation in events
 * - Handling errors during event deletion or participation
 * - Loading the initial list of events
 * - Handling user likes on events
 * - Loading the next page of events (pagination)
 * - Refreshing the list of events
 * - Handle errors in the state.
 *
 * It utilizes a [Mapper] to transform data from the domain layer ([Event]) to the UI layer ([EventUi]).
 *
 * @property mapper A [Mapper] instance used to transform [Event] objects to [EventUi] objects.
 *   This is essential for adapting data from the domain layer to the presentation layer.
 */
class EventReducer @Inject constructor(
    private val mapper: Mapper<Event, EventUi>,
) : Reducer<EventState, EventMessage, EventEffect> {
    override fun reduce(
        old: EventState,
        message: EventMessage
    ): ReducerResult<EventState, EventEffect> {
        return when (message) {
            is EventMessage.Delete -> ReducerResult(
                old.events.filter { it.id != message.event.id }.let { updatedPosts ->
                    old.copy(
                        events = updatedPosts,
                    )
                },
                EventEffect.Delete(message.event),
            )

            is EventMessage.Participant -> {
                val updatedPosts = old.events.map { event ->
                    if (event.id == message.event.id) {
                        event.copy(
                            participatedByMe = !event.participatedByMe,
                            participants = if (event.participatedByMe) event.participants - 1 else event.participants + 1
                        )
                    } else {
                        event
                    }
                }
                ReducerResult(
                    old.copy(
                        events = updatedPosts,
                    ),
                    EventEffect.Participant(message.event)
                )
            }

            is EventMessage.ParticipantResult -> ReducerResult(
                when (val messageResult = message.result) {
                    is Either.Left -> {
                        val resultValue = messageResult.value
                        val event = resultValue.event
                        val updatedPosts = old.events.map {
                            if (it.id == event.id) {
                                event
                            } else {
                                it
                            }
                        }
                        old.copy(
                            events = updatedPosts,
                            singleError = resultValue.error,
                        )
                    }

                    is Either.Right -> {
                        val event = mapper.map(messageResult.value)
                        val updatedPosts = old.events.map {
                            if (it.id == event.id) {
                                event
                            } else {
                                it
                            }
                        }
                        old.copy(
                            events = updatedPosts,
                        )
                    }
                }
            )

            is EventMessage.DeleteError -> ReducerResult(
                buildList(old.events.size + 1) {
                    val event = message.error.event
                    addAll(old.events.filter { it.id > event.id })
                    add(event)
                    addAll(old.events.filter { it.id < event.id })
                }.let { updatedPosts ->
                    old.copy(
                        events = updatedPosts,
                    )
                }
            )

            EventMessage.HandleError -> ReducerResult(
                old.copy(singleError = null)
            )

            is EventMessage.InitialLoaded -> ReducerResult(
                when (val messageResult = message.result) {
                    is Either.Left -> {
                        if (old.events.isNotEmpty()) {
                            old.copy(
                                singleError = messageResult.value,
                                status = EventStatus.Idle(),
                            )
                        } else {
                            old.copy(status = EventStatus.EmptyError(messageResult.value))
                        }
                    }

                    is Either.Right -> {
                        messageResult.value.map(mapper::map).let { updatedPosts ->
                            old.copy(
                                events = updatedPosts,
                                status = EventStatus.Idle(),
                            )
                        }
                    }
                }
            )

            is EventMessage.Like -> {
                val updatedPosts = old.events.map { event ->
                    if (event.id == message.event.id) {
                        event.copy(
                            likedByMe = !event.likedByMe,
                            likes = if (event.likedByMe) event.likes - 1 else event.likes + 1
                        )
                    } else {
                        event
                    }
                }
                ReducerResult(
                    old.copy(
                        events = updatedPosts,
                    ),
                    EventEffect.Like(message.event)
                )
            }

            is EventMessage.LikeResult -> ReducerResult(
                when (val messageResult = message.result) {
                    is Either.Left -> {
                        val resultValue = messageResult.value
                        val event = resultValue.event
                        val updatedPosts = old.events.map {
                            if (it.id == event.id) {
                                event
                            } else {
                                it
                            }
                        }
                        old.copy(
                            events = updatedPosts,
                            singleError = resultValue.error,
                        )
                    }

                    is Either.Right -> {
                        val event = mapper.map(messageResult.value)
                        val updatedPosts = old.events.map {
                            if (it.id == event.id) {
                                event
                            } else {
                                it
                            }
                        }
                        old.copy(
                            events = updatedPosts,
                        )
                    }
                }
            )

            is EventMessage.NextPageLoaded -> ReducerResult(
                when (val messageResult = message.result) {
                    is Either.Left -> {
                        old.copy(
                            status = EventStatus.NextPageError(messageResult.value),
                        )
                    }

                    is Either.Right -> {
                        val updatedPosts = old.events + messageResult.value.map(mapper::map)
                        val isLoadingFinished = message.result.value.size < PAGE_SIZE
                        old.copy(
                            events = updatedPosts,
                            status = EventStatus.Idle(isLoadingFinished),
                        )
                    }
                }
            )

            EventMessage.LoadNextPage -> {
                val isLoadingFinished = (old.status as? EventStatus.Idle)?.isLoadingFinished == true
                val effect = if (isLoadingFinished) {
                    null
                } else
                    EventEffect.LoadNextPage(old.events.last().id, PostReducer.Companion.PAGE_SIZE)

                val status = if (isLoadingFinished) old.status else EventStatus.NextPageLoading
                ReducerResult(
                    old.copy(
                        status = status
                    ),
                    effect,
                )
            }

            EventMessage.Refresh -> ReducerResult(
                old.copy(
                    status = if (old.events.isNotEmpty()) {
                        EventStatus.Refreshing
                    } else {
                        EventStatus.EmptyLoading
                    }
                ),
                EventEffect.LoadInitialPage(PAGE_SIZE),
            )
        }
    }

    companion object {
        const val PAGE_SIZE = 10
    }
}