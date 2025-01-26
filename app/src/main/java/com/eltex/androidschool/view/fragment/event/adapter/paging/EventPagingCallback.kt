package com.eltex.androidschool.view.fragment.event.adapter.paging

import androidx.recyclerview.widget.DiffUtil
import com.eltex.androidschool.view.fragment.event.adapter.event.EventItemCallback

class EventPagingCallback : DiffUtil.ItemCallback<EventPagingModel>() {
    private val delegate = EventItemCallback()

    override fun areItemsTheSame(
        oldItem: EventPagingModel,
        newItem: EventPagingModel
    ): Boolean {
        if (oldItem::class != newItem::class) {
            return false
        }
        return if (oldItem is EventPagingModel.Item && newItem is EventPagingModel.Item) {
            delegate.areItemsTheSame(oldItem.data, newItem.data)
        } else {
            oldItem == newItem
        }
    }

    override fun areContentsTheSame(
        oldItem: EventPagingModel,
        newItem: EventPagingModel
    ): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: EventPagingModel, newItem: EventPagingModel): Any? {
        if (oldItem::class != newItem::class) {
            return null
        }
        return if (oldItem is EventPagingModel.Item && newItem is EventPagingModel.Item) {
            delegate.getChangePayload(oldItem.data, newItem.data)

        } else {
            null
        }
    }
}