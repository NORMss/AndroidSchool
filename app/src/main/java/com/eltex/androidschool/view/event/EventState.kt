package com.eltex.androidschool.view.event

import com.eltex.androidschool.domain.model.Event
import com.eltex.androidschool.utils.datatime.DateSeparators.GroupByDate

data class EventState(
    val events: List<Event> = emptyList(),
    val eventsByDate: List<GroupByDate<Event>> = emptyList(),
    val toast: Pair<Int, Boolean>? = null,
)
