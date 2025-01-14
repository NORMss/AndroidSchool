package com.eltex.androidschool.view.fragment.event

import com.eltex.androidschool.domain.model.Event
import com.eltex.androidschool.view.common.Status
import com.eltex.androidschool.view.model.EventUi
import com.eltex.androidschool.view.util.datetime.DateSeparators.GroupByDate

data class EventState(
    val events: List<Event> = emptyList(),
    val eventsByDate: List<GroupByDate<EventUi>> = emptyList(),
    val status: Status = Status.Idle,
    val singleError: Throwable? = null,
) {
    val isRefreshing: Boolean
        get() = status == Status.Loading && events.isNotEmpty()
    val isEmptyLoading: Boolean
        get() = status == Status.Loading && events.isEmpty()
    val isEmptyError: Boolean
        get() = status is Status.Error && events.isEmpty()
    val isRefreshError: Boolean
        get() = status is Status.Error && events.isNotEmpty()
}
