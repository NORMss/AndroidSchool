package com.eltex.androidschool.domain.repository

import com.eltex.androidschool.domain.model.Event
import kotlinx.coroutines.flow.Flow

interface EventRepository {
    fun getEvents(): Flow<List<Event>>
    fun likeById(id: Long)
    fun participateById(id: Long)
    fun deleteById(id: Long)
    fun editById(id: Long, textContent: String)
    fun addEvent(textContent: String, imageContent: String?)
}