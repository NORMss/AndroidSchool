package com.eltex.androidschool.data.repository

import com.eltex.androidschool.data.remote.api.EventApi
import com.eltex.androidschool.domain.model.Event
import com.eltex.androidschool.domain.repository.EventRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class RemoteEventRepository(
    private val eventApi: EventApi,
) : EventRepository {
    override fun getEvents(): Single<List<Event>> {
        return eventApi.getEvents()
    }

    override fun likeById(
        id: Long,
        isLiked: Boolean
    ): Single<Event> {
        return when (isLiked) {
            true -> eventApi.unlikeById(id)
            false -> eventApi.likeById(id)
        }
    }

    override fun participateById(
        id: Long,
        isParticipated: Boolean,
    ): Single<Event> {
        return when (isParticipated) {
            true -> eventApi.unparticipantById(id)
            false -> eventApi.participantById(id)
        }
    }

    override fun saveEvent(event: Event): Single<Event> {
        return eventApi.save(event)
    }

    override fun deleteById(id: Long): Completable {
        return eventApi.deleteById(id)
    }
}