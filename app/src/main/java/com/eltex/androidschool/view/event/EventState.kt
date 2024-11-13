package com.eltex.androidschool.view.event

import com.eltex.androidschool.domain.model.Event

data class EventState(
    val events: List<Event> = emptyList(),
    val toast: Pair<Int, Boolean>? = null,
)
