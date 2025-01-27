package com.eltex.androidschool.view.fragment.event

import com.eltex.androidschool.view.model.EventUi

data class EventState(
    val events: List<EventUi> = emptyList(),
    val status: EventStatus = EventStatus.Idle(),
    val singleError: Throwable? = null,
) {
    val isRefreshing: Boolean
        get() = status is EventStatus.EmptyError
    val isEmptyLoading: Boolean
        get() = status is EventStatus.EmptyLoading
    val isEmptyError: Boolean
        get() = status is EventStatus.EmptyError
    val emptyError: Throwable? = (status as? EventStatus.EmptyError)?.reason
}
