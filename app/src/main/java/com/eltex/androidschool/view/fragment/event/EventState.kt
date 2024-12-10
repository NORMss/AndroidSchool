package com.eltex.androidschool.view.fragment.event

import com.eltex.androidschool.domain.model.Event
import com.eltex.androidschool.utils.datetime.DateSeparators.GroupByDate
import com.eltex.androidschool.view.common.Status

data class EventState(
    val events: List<Event> = emptyList(),
    val eventsByDate: List<GroupByDate<Event>> = emptyList(),
    val toast: Pair<Int, Boolean>? = null,
    val status: Status = Status.Idle,
) {
    val isRefreshing: Boolean
        get() = status == Status.Loading && events.isEmpty()
    val isEmptyLoading: Boolean
        get() = status == Status.Loading && events.isEmpty()
    val isEmptyError: Boolean
        get() = status is Status.Error && events.isNotEmpty() == true
    val isRefreshingError: Boolean
        get() = status is Status.Error && events.isEmpty()
}
