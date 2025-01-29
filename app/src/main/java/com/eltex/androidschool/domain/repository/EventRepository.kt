package com.eltex.androidschool.domain.repository

import com.eltex.androidschool.domain.model.Event
import com.eltex.androidschool.view.model.FileModel
import kotlinx.datetime.Instant

interface EventRepository {
    suspend fun getEvents(): List<Event>
    suspend fun getEventsNewer(id: Long): List<Event>
    suspend fun getEventsBefore(id: Long, count: Int): List<Event>
    suspend fun getEventsAfter(id: Long, count: Int): List<Event>
    suspend fun getEventsLatest(count: Int): List<Event>
    suspend fun likeById(id: Long, isLiked: Boolean): Event
    suspend fun participateById(id: Long, isParticipated: Boolean): Event
    suspend fun saveEvent(
        id: Long,
        content: String,
        link: String?,
        date: Instant,
        fileModel: FileModel?
    ): Event

    suspend fun deleteById(id: Long)
}