package com.eltex.androidschool.view.fragment.post.adapter.post

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.eltex.androidschool.databinding.PostBinding
import com.eltex.androidschool.domain.model.AttachmentType
import com.eltex.androidschool.view.model.PostUi
import java.util.Locale

class PostViewHolder(private val binding: PostBinding) : ViewHolder(binding.root) {

    fun bind(post: PostUi) {
        val header = binding.header

        binding.contentImage.visibility = View.VISIBLE
        header.username.text = post.author

        header.monogram.load(post.authorAvatar) {
            listener(onSuccess = { _, _ -> header.monogramText.visibility = View.GONE })
        }

        when (post.attachment?.type) {
            AttachmentType.IMAGE -> {
                binding.contentImage.load(post.attachment.url) {
                    crossfade(true)
                    listener(
                        onSuccess = { _, _ ->
                            binding.contentImage.visibility = View.VISIBLE
                            binding.contentVideo.visibility = View.GONE
                        },
                        onError = { _, _ ->
                            binding.contentImage.visibility = View.GONE
                            binding.contentVideo.visibility = View.GONE
                        }
                    )
                }
                binding.contentVideo.visibility = View.GONE
            }

            AttachmentType.VIDEO -> {
                binding.contentVideo.visibility = View.VISIBLE
                binding.contentImage.visibility = View.GONE
            }

            AttachmentType.AUDIO -> {
                binding.contentImage.visibility = View.GONE
                binding.contentVideo.visibility = View.GONE
            }

            null -> {
                binding.contentImage.visibility = View.GONE
                binding.contentVideo.visibility = View.GONE
            }
        }

        header.monogramText.text = post.author.firstOrNull()?.toString() ?: ""
        header.datePublished.text = post.published
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