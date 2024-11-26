package com.eltex.androidschool.data.local.event

import com.eltex.androidschool.domain.model.Event
import kotlinx.coroutines.flow.Flow

interface EventDao {
    suspend fun updateEvent(id: Long, event: Event)
    suspend fun deleteEvent(id: Long)
    suspend fun addEvent(event: Event)
    suspend fun getEvent(id: Long): Event?
    fun getEvents(): Flow<List<Event>>
}