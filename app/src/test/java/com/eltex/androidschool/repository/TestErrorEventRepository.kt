package com.eltex.androidschool.repository

import com.eltex.androidschool.domain.model.Event
import com.eltex.androidschool.domain.repository.EventRepository

interface TestErrorEventRepository : EventRepository {
    override suspend fun getEvents(): List<Event> = error("Not mocked")

    override suspend fun likeById(
        id: Long,
        isLiked: Boolean
    ): Event = error("Not mocked")

    override suspend fun participateById(
        id: Long,
        isParticipated: Boolean
    ): Event = error("Not mocked")

    override suspend fun saveEvent(event: Event): Event = error("Not mocked")

    override suspend fun deleteById(id: Long) = error("Not mocked")
}