package com.eltex.androidschool.view.fragment.event.adapter.event

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.EventBinding
import com.eltex.androidschool.domain.model.AttachmentType
import com.eltex.androidschool.domain.model.Event
import com.eltex.androidschool.domain.model.EventType
import com.eltex.androidschool.utils.datatime.DateTimeStringFormater

class EventViewHolder(private val binding: EventBinding) : ViewHolder(binding.root) {
    fun bind(event: Event) {
        val header = binding.header

        binding.contentImage.visibility = View.VISIBLE
        header.monogramText.visibility = View.VISIBLE
        header.username.text = event.author

        header.monogram.load(event.authorAvatar) {
            listener(onSuccess = { _, _ -> header.monogramText.visibility = View.GONE })
        }

        when (event.attachment?.type) {
            AttachmentType.IMAGE -> binding.contentImage.load(event.attachment.url) {
                listener(
                    onSuccess = { _, _ -> binding.contentImage.visibility = View.VISIBLE },
                    onError = { _, _ -> binding.contentImage.visibility = View.GONE }
                )
            }

            AttachmentType.AUDIO -> {
                binding.play.visibility = View.VISIBLE
                binding.contentImage.visibility = View.GONE
            }

            AttachmentType.VIDEO -> {}
            null -> {
                binding.play.visibility = View.GONE
                binding.contentImage.visibility = View.GONE
                binding.play.visibility = View.GONE
            }
        }

        header.monogramText.text = event.author.first().toString()
        header.datePublished.text = DateTimeStringFormater.dateTimeStringToString(event.published)
        binding.contentText.text = event.content
        binding.onlineStatus.text =
            itemView.context.getString(if (event.type == EventType.ONLINE) R.string.online else R.string.offline)
        binding.datetime.text = DateTimeStringFormater.dateTimeStringToString(event.datetime)

        binding.link.apply {
            text = event.link
            visibility = if (event.link != null) View.VISIBLE else View.GONE
        }

        updateLikedByMe(event.likedByMe)

        updateParticipateByMe(event.participatedByMe)
    }

    fun bind(eventPayload: EventPayload) {
        eventPayload.likedByMe?.let {
            updateLikedByMe(it)
        }
        eventPayload.participatedByMe?.let {
            updateParticipateByMe(it)
        }
    }

    private fun updateLikedByMe(
        likedBeMe: Boolean,
    ) {
        binding.likeButton.isSelected = likedBeMe
        binding.likeButton.text = if (likedBeMe) "1" else "0"
    }

    private fun updateParticipateByMe(
        participateByMe: Boolean,
    ) {
        binding.participate.isSelected = participateByMe
        binding.participate.text = if (participateByMe) "1" else "0"
    }
}