package com.eltex.androidschool.view.post.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eltex.androidschool.databinding.EventBinding
import com.eltex.androidschool.domain.model.Event
import com.eltex.androidschool.view.event.adapter.EventViewHolder

class EventAdapter(
    private val clickLikeListener: (event: Event) -> Unit,
    private val clickShareListener: (event: Event) -> Unit,
    private val clickMoreListener: (event: Event) -> Unit,
    private val clickPlayListener: (event: Event) -> Unit,
    private val clickParticipateListener: (event: Event) -> Unit,
) : RecyclerView.Adapter<EventViewHolder>() {
    var events: List<Event> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = EventBinding.inflate(layoutInflater, parent, false)
        val viewHolder = EventViewHolder(binding)

        setupClickListeners(binding, viewHolder)

        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(events[position])
    }

    override fun getItemCount(): Int = events.size

    private fun setupClickListeners(binding: EventBinding, viewHolder: EventViewHolder) {
        binding.action.likeButton.setOnClickListener { clickLikeListener(events[viewHolder.adapterPosition]) }
        binding.action.shareButton.setOnClickListener { clickShareListener(events[viewHolder.adapterPosition]) }
        binding.header.moreButton.setOnClickListener { clickMoreListener(events[viewHolder.adapterPosition]) }
        binding.play.setOnClickListener { clickPlayListener(events[viewHolder.adapterPosition]) }
        binding.participate.setOnClickListener { clickParticipateListener(events[viewHolder.adapterPosition]) }
    }
}