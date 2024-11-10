package com.eltex.androidschool.view.event

import com.eltex.androidschool.domain.model.Event

data class EventState(
    val event: Event? = null,
    val toast: Pair<Int, Boolean>? = null,
)
