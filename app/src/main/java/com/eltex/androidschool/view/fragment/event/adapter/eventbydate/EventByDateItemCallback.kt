package com.eltex.androidschool.view.fragment.event.adapter.eventbydate

import androidx.recyclerview.widget.DiffUtil.ItemCallback
import com.eltex.androidschool.view.model.EventUi
import com.eltex.androidschool.view.util.datetime.DateSeparators

class EventByDateItemCallback : ItemCallback<DateSeparators.GroupByDate<EventUi>>() {
    override fun areItemsTheSame(
        oldItem: DateSeparators.GroupByDate<EventUi>,
        newItem: DateSeparators.GroupByDate<EventUi>
    ): Boolean = oldItem.date == newItem.date

    override fun areContentsTheSame(
        oldItem: DateSeparators.GroupByDate<EventUi>,
        newItem: DateSeparators.GroupByDate<EventUi>
    ): Boolean = oldItem == newItem

    override fun getChangePayload(
        oldItem: DateSeparators.GroupByDate<EventUi>,
        newItem: DateSeparators.GroupByDate<EventUi>
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