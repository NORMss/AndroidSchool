package com.eltex.androidschool.view.fragment.event.adapter.eventbydate

import androidx.recyclerview.widget.DiffUtil.ItemCallback
import com.eltex.androidschool.domain.model.Event
import com.eltex.androidschool.utils.datatime.DateSeparators

class EventByDateItemCallback : ItemCallback<DateSeparators.GroupByDate<Event>>() {
    override fun areItemsTheSame(
        oldItem: DateSeparators.GroupByDate<Event>,
        newItem: DateSeparators.GroupByDate<Event>
    ): Boolean = oldItem.date == newItem.date

    override fun areContentsTheSame(
        oldItem: DateSeparators.GroupByDate<Event>,
        newItem: DateSeparators.GroupByDate<Event>
    ): Boolean = oldItem == newItem

    override fun getChangePayload(
        oldItem: DateSeparators.GroupByDate<Event>,
        newItem: DateSeparators.GroupByDate<Event>
    ): Any? {
        return EventByDatePayload(
            date = newItem.date.takeIf { it != oldItem.date },
            items = newItem.items.takeIf { it != oldItem.items }
        )
            .takeIf {
                it.isNotEmpty()
            }
    }
}