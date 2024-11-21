package com.eltex.androidschool.domain.local

import com.eltex.androidschool.domain.model.Event
import kotlinx.coroutines.flow.Flow

interface LocalEventManager {
    suspend fun generateNextId(): Long
    fun getEvents(): Flow<List<Event>>
    suspend fun deleteEvent(id: Long)
    suspend fun updateEvent(id: Long, update: (Event) -> Event)
    suspend fun addEvent(event: Event)
}