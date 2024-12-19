package com.eltex.androidschool.view.fragment.event.adapter.event

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.eltex.androidschool.databinding.EventBinding
import com.eltex.androidschool.view.model.EventUi

class EventAdapter(
    private val eventListener: EventListener,
) : ListAdapter<EventUi, EventViewHolder>(
    EventItemCallback()
) {
    interface EventListener {
        fun onLikeClicked(event: EventUi)
        fun onShareClicked(event: EventUi)
        fun onMoreClicked(event: EventUi, view: View)
        fun onPlayClicked(event: EventUi)
        fun onParticipateClicked(event: EventUi)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = EventBinding.inflate(layoutInflater, parent, false)
        val viewHolder = EventViewHolder(binding)

        setupClickListeners(binding, viewHolder)

        return viewHolder
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onBindViewHolder(
        holder: EventViewHolder,
        position: Int,
        payloads: List<Any>
    ) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            payloads.forEach {
                if (it is EventPayload) {
                    holder.bind(it)
                }
            }
        }
    }

    private fun setupClickListeners(binding: EventBinding, viewHolder: EventViewHolder) {
        binding.likeButton.setOnClickListener { eventListener.onLikeClicked(getItem(viewHolder.adapterPosition)) }
        binding.shareButton.setOnClickListener { eventListener.onShareClicked(getItem(viewHolder.adapterPosition)) }
        binding.header.moreButton.setOnClickListener {
            eventListener.onMoreClicked(
                getItem(
                    viewHolder.adapterPosition
                ),
                it,
            )
        }
        binding.play.setOnClickListener { eventListener.onPlayClicked(getItem(viewHolder.adapterPosition)) }
        binding.participate.setOnClickListener {
            eventListener.onParticipateClicked(
                getItem(
                    viewHolder.adapterPosition
                )
            )
        }
    }
}