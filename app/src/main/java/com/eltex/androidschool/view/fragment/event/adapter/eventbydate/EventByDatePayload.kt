package com.eltex.androidschool.view.fragment.event.adapter.eventbydate

import com.eltex.androidschool.domain.model.Event
import kotlinx.datetime.Instant

data class EventByDatePayload(
    val date: Instant? = null,
    val items: List<Event>? = null,
) {
    fun isNotEmpty(): Boolean = date != null || items != null
}