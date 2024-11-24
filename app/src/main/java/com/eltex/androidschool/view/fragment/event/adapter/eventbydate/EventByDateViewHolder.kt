package com.eltex.androidschool.view.fragment.event.adapter.eventbydate

import android.view.View
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.EventsByDateBinding
import com.eltex.androidschool.domain.model.Event
import com.eltex.androidschool.utils.datatime.DateSeparators
import com.eltex.androidschool.view.common.OffsetDecoration
import com.eltex.androidschool.view.fragment.event.adapter.event.EventAdapter

class EventByDateViewHolder(
    eventListener: EventAdapter.EventListener,
    private val binding: EventsByDateBinding,
) : ViewHolder(binding.root) {
    private val viewPool = RecycledViewPool()
    private val postAdapter = EventAdapter(
        eventListener = eventListener,
    )

    init {
        binding.events.setRecycledViewPool(viewPool)
        binding.events.adapter = postAdapter
        binding.events.addItemDecoration(
            OffsetDecoration(
                horizontalOffset = binding.events.resources.getDimensionPixelSize(R.dimen.zero),
                verticalOffset = binding.events.resources.getDimensionPixelSize(R.dimen.list_offset)
            )
        )
    }

    fun bind(groupByDate: DateSeparators.GroupByDate<Event>) {
        postAdapter.submitList(groupByDate.items)
        binding.separator.date.apply {
            text = groupByDate.date
            visibility = View.VISIBLE
        }
    }

    fun bind(eventByDatePayload: EventByDatePayload) {
        eventByDatePayload.date?.let { date ->
            binding.separator.date.text = date
        }
        eventByDatePayload.items?.let { items ->
            postAdapter.submitList(items)
        }
    }
}