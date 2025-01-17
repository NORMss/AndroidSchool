package com.eltex.androidschool.view.fragment.event.reducer

import arrow.core.Either
import com.eltex.androidschool.domain.mapper.GroupByDateMapper
import com.eltex.androidschool.domain.mapper.Mapper
import com.eltex.androidschool.domain.model.Event
import com.eltex.androidschool.mvi.Reducer
import com.eltex.androidschool.mvi.ReducerResult
import com.eltex.androidschool.view.fragment.event.EventEffect
import com.eltex.androidschool.view.fragment.event.EventMessage
import com.eltex.androidschool.view.fragment.event.EventState
import com.eltex.androidschool.view.fragment.event.EventStatus
import com.eltex.androidschool.view.model.EventUi

class EventReducer(
    private val mapper: Mapper<Event, EventUi>,
    private val mapperByDate: GroupByDateMapper<EventUi, EventUi>,
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
                        eventsByDate = mapperByDate.map(updatedPosts),
                    )
                },
                EventEffect.Delete(message.event),
            )

            is EventMessage.Participant -> {
                val updatedPosts = old.events.map { event ->
                    if (event.id == message.event.id) {
                        event.copy(
                            participatedByMe = !event.participatedByMe,
                            likes = if (event.participatedByMe) event.participants - 1 else event.participants + 1
                        )
                    } else {
                        event
                    }
                }
                ReducerResult(
                    old.copy(
                        events = updatedPosts,
                        eventsByDate = mapperByDate.map(updatedPosts),
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
                            eventsByDate = mapperByDate.map(updatedPosts),
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
                            eventsByDate = mapperByDate.map(updatedPosts),
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
                        eventsByDate = mapperByDate.map(updatedPosts)
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
                                status = EventStatus.Idle,
                            )
                        } else {
                            old.copy(status = EventStatus.EmptyError(messageResult.value))
                        }
                    }

                    is Either.Right -> {
                        messageResult.value.map(mapper::map).let { updatedPosts ->
                            old.copy(
                                events = updatedPosts,
                                eventsByDate = mapperByDate.map(updatedPosts),
                                status = EventStatus.Idle,
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
                        eventsByDate = mapperByDate.map(updatedPosts),
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
                            eventsByDate = mapperByDate.map(updatedPosts),
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
                            eventsByDate = mapperByDate.map(updatedPosts),
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
                        old.copy(
                            events = updatedPosts,
                            eventsByDate = mapperByDate.map(updatedPosts),
                            status = EventStatus.Idle,
                        )
                    }
                }
            )

            EventMessage.LoadNextPage -> ReducerResult(
                old.copy(
                    status = EventStatus.NextPageLoading
                ),
                EventEffect.LoadNextPage(old.events.last().id, PAGE_SIZE),
            )

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