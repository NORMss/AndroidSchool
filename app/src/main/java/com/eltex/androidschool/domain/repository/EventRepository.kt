package com.eltex.androidschool.domain.repository

import com.eltex.androidschool.domain.model.Attachment
import com.eltex.androidschool.domain.model.Event
import com.eltex.androidschool.utils.remote.Callback

interface EventRepository {
    fun getEvents(callback: Callback<List<Event>>)
    fun likeById(id: Long, callback: Callback<Event>)
    fun participateById(id: Long, callback: Callback<Event>)
    fun saveEvent(
        id: Long,
        content: String,
        attachment: Attachment?,
        link: String?,
        callback: Callback<Event>
    )

    fun deleteById(id: Long, callback: Callback<Unit>)
}