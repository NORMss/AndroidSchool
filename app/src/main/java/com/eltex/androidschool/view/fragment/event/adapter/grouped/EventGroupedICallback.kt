package com.eltex.androidschool.view.fragment.event.adapter.grouped

import androidx.recyclerview.widget.DiffUtil
import com.eltex.androidschool.view.fragment.event.adapter.event.EventItemCallback

class EventGroupedICallback : DiffUtil.ItemCallback<EventListItem>() {
    private val delegate = EventItemCallback()

    override fun areItemsTheSame(
        oldItem: EventListItem,
        newItem: EventListItem
    ): Boolean {
        if (oldItem::class != newItem::class) {
            return false
        }
        return if (oldItem is EventListItem.ItemEvent && newItem is EventListItem.ItemEvent) {
            delegate.areItemsTheSame(oldItem.data, newItem.data)
        } else {
            oldItem == newItem
        }
    }

    override fun areContentsTheSame(
        oldItem: EventListItem,
        newItem: EventListItem
    ): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: EventListItem, newItem: EventListItem): Any? {
        if (oldItem::class != newItem::class) {
            return null
        }
        return if (oldItem is EventListItem.ItemEvent && newItem is EventListItem.ItemEvent) {
            delegate.getChangePayload(oldItem.data, newItem.data)

        } else {
            null
        }
    }
}