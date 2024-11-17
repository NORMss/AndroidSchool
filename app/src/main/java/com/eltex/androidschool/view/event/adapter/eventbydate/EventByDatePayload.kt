package com.eltex.androidschool.view.event.adapter.eventbydate

import com.eltex.androidschool.domain.model.Event

data class EventByDatePayload(
    val date: String? = null,
    val items: List<Event>? = null,
) {
    fun isNotEmpty(): Boolean = date != null || items != null
}