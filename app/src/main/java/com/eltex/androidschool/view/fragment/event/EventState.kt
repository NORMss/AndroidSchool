package com.eltex.androidschool.view.fragment.event

import com.eltex.androidschool.domain.model.Event
import com.eltex.androidschool.utils.datetime.DateSeparators.GroupByDate

data class EventState(
    val events: List<Event> = emptyList(),
    val eventsByDate: List<GroupByDate<Event>> = emptyList(),
    val toast: Pair<Int, Boolean>? = null,
)
