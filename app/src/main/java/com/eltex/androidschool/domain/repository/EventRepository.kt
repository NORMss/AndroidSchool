package com.eltex.androidschool.domain.repository

import com.eltex.androidschool.domain.model.Event

interface EventRepository {
    suspend fun getEvents(): List<Event>
    suspend fun likeById(id: Long, isLiked: Boolean): Event
    suspend fun participateById(id: Long, isParticipated: Boolean): Event
    suspend fun saveEvent(event: Event): Event
    suspend fun deleteById(id: Long)
}