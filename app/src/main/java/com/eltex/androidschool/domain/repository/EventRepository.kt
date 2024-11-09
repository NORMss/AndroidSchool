package com.eltex.androidschool.domain.repository

import com.eltex.androidschool.domain.model.Event
import kotlinx.coroutines.flow.Flow

interface EventRepository {
    fun getEvent(): Flow<Event>
    fun like()
    fun participate()
}