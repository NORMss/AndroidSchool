package com.eltex.androidschool.view.fragment.event.adapter.event

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.EventBinding
import com.eltex.androidschool.domain.model.AttachmentType
import com.eltex.androidschool.domain.model.EventType
import com.eltex.androidschool.view.model.EventUi
import java.util.Locale

class EventViewHolder(private val binding: EventBinding) : ViewHolder(binding.root) {
    fun bind(event: EventUi) {
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
                    onSuccess = { _, _ ->
                        binding.contentImage.visibility = View.VISIBLE
                        binding.contentVideo.visibility = View.GONE
                        binding.play.visibility = View.GONE
                    },
                    onError = { _, _ ->
                        binding.contentImage.visibility = View.GONE
                        binding.contentVideo.visibility = View.GONE
                        binding.play.visibility = View.GONE
                    }
                )

            }

            AttachmentType.AUDIO -> {
                binding.contentImage.visibility = View.GONE
                binding.contentVideo.visibility = View.GONE
                binding.play.visibility = View.VISIBLE
            }

            AttachmentType.VIDEO -> {
                binding.contentImage.visibility = View.GONE
                binding.contentVideo.visibility = View.VISIBLE
                binding.play.visibility = View.GONE
            }

            null -> {
                binding.contentImage.visibility = View.GONE
                binding.contentVideo.visibility = View.GONE
                binding.play.visibility = View.GONE

            }
        }

        header.monogramText.text = event.author.first().toString()
        header.datePublished.text = event.published
        binding.contentText.text = event.content
        binding.onlineStatus.text =
            itemView.context.getString(if (event.type == EventType.ONLINE) R.string.online else R.string.offline)
        binding.datetime.text = event.datetime

        binding.link.apply {
            text = event.link
            visibility = if (event.link != null) View.VISIBLE else View.GONE
        }

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