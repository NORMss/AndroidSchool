package com.eltex.androidschool.view.fragment.event.adapter.paging

import com.eltex.androidschool.view.model.EventUi
import kotlinx.datetime.Instant

sealed class EventPagingModel {
    data class Header(val date: Instant) : EventPagingModel()
    data class Item(val data: EventUi) : EventPagingModel()
    data object Loading : EventPagingModel()
    data class Error(val reason: Throwable) : EventPagingModel()
}