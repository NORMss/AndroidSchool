package com.eltex.androidschool.view.fragment.event.adapter.event

import androidx.recyclerview.widget.DiffUtil
import com.eltex.androidschool.view.model.EventUi

class EventItemCallback : DiffUtil.ItemCallback<EventUi>() {
    override fun areItemsTheSame(oldItem: EventUi, newItem: EventUi): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: EventUi, newItem: EventUi): Boolean =
        oldItem == newItem

    override fun getChangePayload(oldItem: EventUi, newItem: EventUi): Any? {
        return EventPayload(
            likedByMe = newItem.likedByMe.takeIf { it != oldItem.likedByMe },
            likes = newItem.likes.takeIf { it != oldItem.likes },
            participatedByMe = newItem.participatedByMe.takeIf { it != oldItem.participatedByMe },
            participants = newItem.participants.takeIf { it != oldItem.participants },
        )
            .takeIf {
                it.isNotEmpty()
            }
    }
}