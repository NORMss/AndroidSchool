package com.eltex.androidschool.view.fragment.event.adapter.eventbydate

import com.eltex.androidschool.view.model.EventUi
import kotlinx.datetime.Instant

data class EventByDatePayload(
    val date: Instant? = null,
    val items: List<EventUi>? = null,
) {
    fun isNotEmpty(): Boolean = date != null || items != null
}