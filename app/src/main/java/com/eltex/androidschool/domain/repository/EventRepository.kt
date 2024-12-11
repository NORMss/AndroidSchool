package com.eltex.androidschool.domain.repository

import com.eltex.androidschool.domain.model.Attachment
import com.eltex.androidschool.domain.model.Event
import com.eltex.androidschool.utils.remote.Callback
import kotlinx.datetime.Instant

interface EventRepository {
    fun getEvents(callback: Callback<List<Event>>)
    fun likeById(id: Long, isLiked: Boolean, callback: Callback<Event>)
    fun participateById(id: Long,isParticipated: Boolean, callback: Callback<Event>)
    fun saveEvent(
        id: Long,
        content: String,
        dateTime: Instant,
        attachment: Attachment?,
        link: String?,
        callback: Callback<Event>
    )

    fun deleteById(id: Long, callback: Callback<Unit>)
}