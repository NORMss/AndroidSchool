package com.eltex.androidschool.view.event.adapter.eventbydate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.eltex.androidschool.databinding.FragmentEventBinding
import com.eltex.androidschool.domain.model.Event
import com.eltex.androidschool.utils.datatime.DateSeparators

class EventByDateAdapter(
    private val clickLikeListener: (event: Event) -> Unit,
    private val clickShareListener: (event: Event) -> Unit,
    private val clickMoreListener: (event: Event) -> Unit,
    private val clickPlayListener: (event: Event) -> Unit,
    private val clickParticipateListener: (event: Event) -> Unit,
) : ListAdapter<DateSeparators.GroupByDate<Event>, EventByDateViewHolder>(
    EventByDateItemCallback()
) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EventByDateViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = FragmentEventBinding.inflate(layoutInflater, parent, false)
        val viewHolder = EventByDateViewHolder(
            clickLikeListener = clickLikeListener,
            clickShareListener = clickShareListener,
            clickMoreListener = clickMoreListener,
            clickPlayListener = clickPlayListener,
            clickParticipateListener = clickParticipateListener,
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