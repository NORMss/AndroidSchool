package com.eltex.androidschool.view.fragment.event.adapter.event

import androidx.recyclerview.widget.DiffUtil
import com.eltex.androidschool.domain.model.Event

class EventItemCallback : DiffUtil.ItemCallback<Event>() {
    override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean = oldItem == newItem

    override fun getChangePayload(oldItem: Event, newItem: Event): Any? {
        return EventPayload(
            likedByMe = newItem.likedByMe.takeIf { it != oldItem.likedByMe },
            participatedByMe = newItem.participatedByMe.takeIf { it != oldItem.participatedByMe }
        )
            .takeIf {
                it.isNotEmpty()
            }
    }
}