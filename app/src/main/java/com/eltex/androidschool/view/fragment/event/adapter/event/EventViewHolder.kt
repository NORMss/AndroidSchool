package com.eltex.androidschool.view.fragment.event.adapter.event

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.EventBinding
import com.eltex.androidschool.domain.model.AttachmentType
import com.eltex.androidschool.domain.model.EventType
import com.eltex.androidschool.view.model.EventUi
import com.eltex.androidschool.view.util.datetime.DateTimeStringFormater
import java.util.Locale

class EventViewHolder(private val binding: EventBinding) : ViewHolder(binding.root) {
    fun bind(event: EventUi) {
        val header = binding.header
        header.monogramText.isVisible = true
        header.username.text = event.author

        header.monogram.load(event.authorAvatar) {
            listener(onSuccess = { _, _ -> header.monogramText.isVisible = false })
        }

        when (event.attachment?.type) {
            AttachmentType.IMAGE -> {
                binding.contentImage.isVisible = true
                binding.contentImage.load(event.attachment.url) {
                    crossfade(true)
                    placeholder(R.drawable.image_loading)
                    error(R.drawable.image_error)
                }
            }

            AttachmentType.AUDIO -> {
                binding.contentImage.isVisible = false
                binding.contentVideo.isVisible = false
                binding.play.isVisible = true
            }

            AttachmentType.VIDEO -> {
                binding.contentImage.isVisible = false
                binding.contentVideo.isVisible = true
                binding.play.isVisible = false
            }

            null -> {
                binding.contentImage.isVisible = false
                binding.contentVideo.isVisible = false
                binding.play.isVisible = false

            }
        }

        header.monogramText.text = event.author.first().toString()
        header.datePublished.text = DateTimeStringFormater.dateTimeToString(event.published)
        binding.contentText.text = event.content
        binding.onlineStatus.text =
            itemView.context.getString(if (event.type == EventType.ONLINE) R.string.online else R.string.offline)
        binding.datetime.text = event.datetime

        if (event.link != null && event.link.isNotBlank()) {
            binding.link.apply {
                text = event.link
                isVisible = true
            }
        } else binding.link.isVisible = false

        updateLikedByMe(event.likedByMe)

        updateParticipateByMe(event.participatedByMe)

        updateParticipantsIds(event.participants)

        updateLikeOwnerIds(event.likes)
    }

    fun bind(eventPayload: EventPayload) {
        eventPayload.likedByMe?.let {
            updateLikedByMe(it)
        }
        eventPayload.participatedByMe?.let {
            updateParticipateByMe(it)
        }
        eventPayload.participants?.let {
            updateParticipantsIds(it)
        }
        eventPayload.likes?.let {
            updateLikeOwnerIds(it)
        }
    }

    private fun updateLikedByMe(
        likedBeMe: Boolean,
    ) {
        binding.likeButton.isSelected = likedBeMe
    }

    private fun updateLikeOwnerIds(likes: Int) {
        binding.likeButton.text = String.format(Locale.getDefault(), "%d", likes)
    }

    private fun updateParticipateByMe(
        participateByMe: Boolean,
    ) {
        binding.participate.isSelected = participateByMe
    }

    private fun updateParticipantsIds(
        participants: Int,
    ) {
        binding.participate.text = String.format(Locale.getDefault(), "%d", participants)
    }
}