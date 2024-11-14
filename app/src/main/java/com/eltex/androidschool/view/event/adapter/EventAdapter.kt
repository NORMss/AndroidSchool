package com.eltex.androidschool.view.event.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.eltex.androidschool.databinding.EventBinding
import com.eltex.androidschool.domain.model.Event

class EventAdapter(
    private val clickLikeListener: (event: Event) -> Unit,
    private val clickShareListener: (event: Event) -> Unit,
    private val clickMoreListener: (event: Event) -> Unit,
    private val clickPlayListener: (event: Event) -> Unit,
    private val clickParticipateListener: (event: Event) -> Unit,
) : ListAdapter<Event, EventViewHolder>(
    EventItemCallback()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = EventBinding.inflate(layoutInflater, parent, false)
        val viewHolder = EventViewHolder(binding)

        setupClickListeners(binding, viewHolder)

        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    private fun setupClickListeners(binding: EventBinding, viewHolder: EventViewHolder) {
        binding.action.likeButton.setOnClickListener { clickLikeListener(getItem(viewHolder.adapterPosition)) }
        binding.action.shareButton.setOnClickListener { clickShareListener(getItem(viewHolder.adapterPosition)) }
        binding.header.moreButton.setOnClickListener { clickMoreListener(getItem(viewHolder.adapterPosition)) }
        binding.play.setOnClickListener { clickPlayListener(getItem(viewHolder.adapterPosition)) }
        binding.participate.setOnClickListener { clickParticipateListener(getItem(viewHolder.adapterPosition)) }
    }
}