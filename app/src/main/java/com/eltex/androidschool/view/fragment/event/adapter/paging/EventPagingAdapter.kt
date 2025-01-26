package com.eltex.androidschool.view.fragment.event.adapter.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.EventBinding
import com.eltex.androidschool.databinding.EventSkeletonBinding
import com.eltex.androidschool.databinding.ItemErrorBinding
import com.eltex.androidschool.databinding.SeparatorDateBinding
import com.eltex.androidschool.view.fragment.common.ErrorViewHolder
import com.eltex.androidschool.view.fragment.common.SeparatorDateViewHolder
import com.eltex.androidschool.view.fragment.event.adapter.event.EventAdapter
import com.eltex.androidschool.view.fragment.event.adapter.event.EventPayload
import com.eltex.androidschool.view.fragment.event.adapter.event.EventViewHolder

class EventPagingAdapter(
    private val eventListener: EventAdapter.EventListener,
) : ListAdapter<EventPagingModel, RecyclerView.ViewHolder>(EventPagingCallback()) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is EventPagingModel.Header -> R.layout.separator_date
            is EventPagingModel.Item -> R.layout.event
            is EventPagingModel.Error -> R.layout.item_error
            EventPagingModel.Loading -> R.layout.event_skeleton
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val eventBinding = EventBinding.inflate(layoutInflater, parent, false)
        val separatorDateBinding = SeparatorDateBinding.inflate(layoutInflater, parent, false)
        val itemErrorBinding = ItemErrorBinding.inflate(layoutInflater, parent, false)
        val progressBinding = EventSkeletonBinding.inflate(layoutInflater, parent, false)

        return when (viewType) {
            R.layout.separator_date -> {
                SeparatorDateViewHolder(separatorDateBinding)
            }

            R.layout.event -> {
                val eventViewHolder = EventViewHolder(eventBinding)
                setupClickListenersByEvent(eventBinding, eventViewHolder)
                eventViewHolder
            }

            R.layout.item_error -> {
                val errorViewHolder = ErrorViewHolder(itemErrorBinding)
                itemErrorBinding.retry.setOnClickListener {
                    eventListener.onLoadNextPage()
                }
                errorViewHolder
            }

            R.layout.event_skeleton -> {
                EventProgressViewHolder(progressBinding)
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
            is EventPagingModel.Header -> {
                (holder as SeparatorDateViewHolder).bind(item.date)
            }

            is EventPagingModel.Item -> {
                (holder as EventViewHolder).bind(item.data)
            }

            is EventPagingModel.Error -> {
                (holder as ErrorViewHolder).bind(item.reason)
            }

            EventPagingModel.Loading -> Unit
        }
    }

    private fun setupClickListenersByEvent(binding: EventBinding, viewHolder: EventViewHolder) {
        binding.likeButton.setOnClickListener {
            val item = getItem(viewHolder.adapterPosition) as? EventPagingModel.Item
            item?.data?.let(eventListener::onLikeClicked)
        }
        binding.participate.setOnClickListener {
            val item = getItem(viewHolder.adapterPosition) as? EventPagingModel.Item
            item?.data?.let(eventListener::onParticipateClicked)
        }
        binding.shareButton.setOnClickListener {
            val item = getItem(viewHolder.adapterPosition) as? EventPagingModel.Item
            item?.data?.let(eventListener::onShareClicked)
        }
        binding.header.moreButton.setOnClickListener { view ->
            val item = getItem(viewHolder.adapterPosition) as? EventPagingModel.Item
            item?.let { item ->
                eventListener.onMoreClicked(
                    item.data,
                    view,
                )
            }
        }
    }
}