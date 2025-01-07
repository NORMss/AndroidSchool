package com.eltex.androidschool.data.repository

import com.eltex.androidschool.data.remote.api.EventApi
import com.eltex.androidschool.domain.model.Event
import com.eltex.androidschool.domain.repository.EventRepository

class RemoteEventRepository(
    private val eventApi: EventApi,
) : EventRepository {
    override suspend fun getEvents(): List<Event> {
        return eventApi.getEvents()
    }

    override suspend fun likeById(
        id: Long,
        isLiked: Boolean,
    ): Event {
        return when (isLiked) {
            true -> eventApi.unlikeById(id)
            false -> eventApi.likeById(id)
        }
    }

    override suspend fun participateById(
        id: Long,
        isParticipated: Boolean,
    ): Event {
        return when (isParticipated) {
            true -> eventApi.unparticipantById(id)
            false -> eventApi.participantById(id)
        }
    }

    override suspend fun saveEvent(event: Event): Event {
        return eventApi.save(event)
    }

    override suspend fun deleteById(id: Long) {
        return eventApi.deleteById(id)
    }
}