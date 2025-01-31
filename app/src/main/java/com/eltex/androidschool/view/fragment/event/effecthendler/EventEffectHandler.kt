@file:OptIn(ExperimentalCoroutinesApi::class)

package com.eltex.androidschool.view.fragment.event.effecthendler

import arrow.core.left
import arrow.core.right
import com.eltex.androidschool.domain.repository.EventRepository
import com.eltex.androidschool.mvi.EffectHandler
import com.eltex.androidschool.view.fragment.event.EventEffect
import com.eltex.androidschool.view.fragment.event.EventMessage
import com.eltex.androidschool.view.model.EventWithError
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.merge
import javax.inject.Inject

class EventEffectHandler @Inject constructor(
    private val repository: EventRepository,
) : EffectHandler<EventEffect, EventMessage> {
    override fun connect(effects: Flow<EventEffect>): Flow<EventMessage> {
        return listOf(
            handleInitialPage(effects),
            handleNextPage(effects),
            handleDelete(effects),
            handleLike(effects),
            handleParticipant(effects),
        )
            .merge()
    }

    private fun handleDelete(effects: Flow<EventEffect>): Flow<EventMessage.DeleteError> =
        effects.filterIsInstance<EventEffect.Delete>()
            .mapLatest {
                try {
                    repository.deleteById(it.event.id)
                } catch (e: Exception) {
                    if (e is CancellationException) throw e
                    EventMessage.DeleteError(EventWithError(it.event, e))
                }
            }
            .filterIsInstance<EventMessage.DeleteError>()

    private fun handleLike(effects: Flow<EventEffect>): Flow<EventMessage.LikeResult> =
        effects.filterIsInstance<EventEffect.Like>()
            .mapLatest {
                EventMessage.LikeResult(
                    try {
                        repository.likeById(it.event.id, it.event.likedByMe)
                            .right()
                    } catch (e: Exception) {
                        if (e is CancellationException) throw e
                        EventWithError(it.event, e)
                            .left()
                    }
                )
            }

    private fun handleParticipant(effects: Flow<EventEffect>): Flow<EventMessage.ParticipantResult> =
        effects.filterIsInstance<EventEffect.Participant>()
            .mapLatest {
                EventMessage.ParticipantResult(
                    try {
                        repository.participateById(it.event.id, it.event.participatedByMe)
                            .right()
                    } catch (e: Exception) {
                        if (e is CancellationException) throw e
                        EventWithError(it.event, e)
                            .left()
                    }
                )
            }

    private fun handleNextPage(effects: Flow<EventEffect>): Flow<EventMessage.NextPageLoaded> =
        effects.filterIsInstance<EventEffect.LoadNextPage>()
            .mapLatest {
                EventMessage.NextPageLoaded(
                    try {
                        repository.getEventsBefore(it.id, it.count)
                            .right()
                    } catch (e: Exception) {
                        if (e is CancellationException) throw e
                        e.left()
                    }
                )
            }

    private fun handleInitialPage(effects: Flow<EventEffect>): Flow<EventMessage.InitialLoaded> =
        effects.filterIsInstance<EventEffect.LoadInitialPage>()
            .mapLatest {
                EventMessage.InitialLoaded(
                    try {
                        repository.getEventsLatest(it.count)
                            .right()
                    } catch (e: Exception) {
                        if (e is CancellationException) throw e
                        e.left()
                    }
                )
            }
}