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

    override suspend fun getEventsNewer(id: Long): List<Event> {
        return eventApi.getEventsNewer(id)
    }

    override suspend fun getEventsBefore(
        id: Long,
        count: Int
    ): List<Event> {
        return eventApi.getEventsBefore(id, count)
    }

    override suspend fun getEventsAfter(
        id: Long,
        count: Int
    ): List<Event> {
        return eventApi.getEventsAfter(id, count)
    }

    override suspend fun getEventsLatest(count: Int): List<Event> {
        return eventApi.getEventsLatest(count)
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