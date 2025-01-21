package com.eltex.androidschool.view.fragment.event.adapter.grouped

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.EventBinding
import com.eltex.androidschool.databinding.SeparatorDateBinding
import com.eltex.androidschool.view.fragment.common.SeparatorDateViewHolder
import com.eltex.androidschool.view.fragment.event.adapter.event.EventAdapter
import com.eltex.androidschool.view.fragment.event.adapter.event.EventPayload
import com.eltex.androidschool.view.fragment.event.adapter.event.EventViewHolder

class EventGroupedAdapter(
    private val eventListener: EventAdapter.EventListener,
) : ListAdapter<EventListItem, RecyclerView.ViewHolder>(EventGroupedICallback()) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is EventListItem.Header -> R.layout.separator_date
            is EventListItem.ItemEvent -> R.layout.event
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val eventBinding = EventBinding.inflate(layoutInflater, parent, false)
        val separatorDateBinding = SeparatorDateBinding.inflate(layoutInflater, parent, false)

        return when (viewType) {
            R.layout.separator_date -> {
                SeparatorDateViewHolder(separatorDateBinding)
            }

            R.layout.event -> {
                val eventViewHolder = EventViewHolder(eventBinding)
                setupClickListenersByEvent(eventBinding, eventViewHolder)
                eventViewHolder
            }

            else -> error("Unknown viewType: $viewType")
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: List<Any?>
    ) {
        if (payloads.isEmpty())
            onBindViewHolder(holder, position)
        else
            payloads.forEach {
                if (it is EventPayload) {
                    (holder as? EventViewHolder)?.bind(it)
                }
            }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        when (val item = getItem(position)) {
            is EventListItem.Header -> {
                (holder as SeparatorDateViewHolder).bind(item.date)
            }

            is EventListItem.ItemEvent -> {
                (holder as EventViewHolder).bind(item.data)
            }
        }
    }

    private fun setupClickListenersByEvent(binding: EventBinding, viewHolder: EventViewHolder) {
        binding.likeButton.setOnClickListener {
            val item = getItem(viewHolder.adapterPosition) as? EventListItem.ItemEvent
            item?.data?.let(eventListener::onLikeClicked)
        }
        binding.participate.setOnClickListener {
            val item = getItem(viewHolder.adapterPosition) as? EventListItem.ItemEvent
            item?.data?.let(eventListener::onParticipateClicked)
        }
        binding.shareButton.setOnClickListener {
            val item = getItem(viewHolder.adapterPosition) as? EventListItem.ItemEvent
            item?.data?.let(eventListener::onShareClicked)
        }
        binding.header.moreButton.setOnClickListener { view ->
            val item = getItem(viewHolder.adapterPosition) as? EventListItem.ItemEvent
            item?.let { item ->
                eventListener.onMoreClicked(
                    item.data,
                    view,
                )
            }
        }
    }
}