package com.eltex.androidschool.view.event.adapter

import androidx.recyclerview.widget.DiffUtil
import com.eltex.androidschool.domain.model.Event

class EventItemCallback : DiffUtil.ItemCallback<Event>() {
    override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean = oldItem == newItem
}