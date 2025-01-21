package com.eltex.androidschool.view.fragment.event.adapter.grouped

import com.eltex.androidschool.view.model.EventUi
import kotlinx.datetime.Instant

sealed class EventListItem {
    data class Header(val date: Instant) : EventListItem()
    data class ItemEvent(val data: EventUi) : EventListItem()
}