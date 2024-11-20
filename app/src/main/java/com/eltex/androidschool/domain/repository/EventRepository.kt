package com.eltex.androidschool.domain.repository

import com.eltex.androidschool.domain.model.Event
import kotlinx.coroutines.flow.Flow

interface EventRepository {
    suspend fun likeById(id: Long)
    suspend fun editEventById(id: Long, textContent: String)
    fun getEvents(): Flow<List<Event>>
    suspend fun deleteEventById(id: Long)
    suspend fun addEvent(textContent: String, imageContent: String?)
    suspend fun participateById(id: Long)
}