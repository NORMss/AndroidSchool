package com.eltex.androidschool.view.fragment.post.adapter.post

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.PostBinding
import com.eltex.androidschool.domain.model.AttachmentType
import com.eltex.androidschool.view.model.PostUi
import com.eltex.androidschool.view.util.datetime.DateTimeStringFormatter
import java.util.Locale

class PostViewHolder(private val binding: PostBinding) : ViewHolder(binding.root) {

    fun bind(post: PostUi) {
        val header = binding.header

        header.username.text = post.author
        header.monogramText.isVisible = true
        header.monogram.load(post.authorAvatar) {
            listener(onSuccess = { _, _ -> header.monogramText.isVisible = false })
        }

        when (post.attachment?.type) {
            AttachmentType.IMAGE -> {
                binding.contentImage.isVisible = true
                binding.contentImage.load(post.attachment.url) {
                    crossfade(true)
                    placeholder(R.drawable.image_loading)
                    error(R.drawable.image_error)
                }
            }

            AttachmentType.VIDEO -> {
                binding.contentVideo.isVisible = true
                binding.contentImage.isVisible = false
            }

            AttachmentType.AUDIO -> {
                binding.contentImage.isVisible = false
                binding.contentVideo.isVisible = false
            }

            null -> {
                binding.contentImage.isVisible = false
                binding.contentVideo.isVisible = false
            }
        }

        header.monogramText.text = post.author.firstOrNull()?.toString() ?: ""
        header.datePublished.text = DateTimeStringFormatter.default().format(post.published)
        binding.contentText.text = post.content

        updateLikedByMe(post.likedByMe)

        updateLikeOwnerIds(post.likes)
    }

    fun bind(payload: PostPayload) {
        payload.likedByMe?.let { likedByMe ->
            updateLikedByMe(likedByMe)
        }
        payload.likes?.let {
            updateLikeOwnerIds(it)
        }
    }

    private fun updateLikedByMe(likedByMe: Boolean) {
        binding.likeButton.isSelected = likedByMe
    }

    private fun updateLikeOwnerIds(likes: Int) {
        binding.likeButton.text = String.format(Locale.getDefault(), "%d", likes)
    }
}