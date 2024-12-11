package com.eltex.androidschool.domain.repository

import com.eltex.androidschool.domain.model.Event
import com.eltex.androidschool.utils.remote.Callback

interface EventRepository {
    fun getEvents(callback: Callback<List<Event>>)
    fun likeById(id: Long, isLiked: Boolean, callback: Callback<Event>)
    fun participateById(id: Long, isParticipated: Boolean, callback: Callback<Event>)
    fun saveEvent(
        event: Event,
        callback: Callback<Event>
    )

    fun deleteById(id: Long, callback: Callback<Unit>)
}