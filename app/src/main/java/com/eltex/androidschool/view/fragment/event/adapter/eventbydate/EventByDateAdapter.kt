package com.eltex.androidschool.view.fragment.event.adapter.eventbydate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.eltex.androidschool.databinding.EventsByDateBinding
import com.eltex.androidschool.view.util.datetime.DateSeparators
import com.eltex.androidschool.view.fragment.event.adapter.event.EventAdapter
import com.eltex.androidschool.view.model.EventUi

class EventByDateAdapter(
    private val eventListener: EventAdapter.EventListener,
) : ListAdapter<DateSeparators.GroupByDate<EventUi>, EventByDateViewHolder>(
    EventByDateItemCallback()
) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EventByDateViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = EventsByDateBinding.inflate(layoutInflater, parent, false)
        val viewHolder = EventByDateViewHolder(
            eventListener = eventListener,
            binding = binding,
        )

        return viewHolder
    }

    override fun onBindViewHolder(
        holder: EventByDateViewHolder,
        position: Int,
        payloads: List<Any>
    ) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            payloads.forEach {
                if (it is EventByDatePayload) {
                    holder.bind(it)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: EventByDateViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}