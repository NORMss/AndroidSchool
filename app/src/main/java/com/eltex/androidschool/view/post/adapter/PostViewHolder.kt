package com.eltex.androidschool.view.post.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.eltex.androidschool.databinding.PostBinding
import com.eltex.androidschool.domain.model.AttachmentType
import com.eltex.androidschool.domain.model.Post

class PostViewHolder(private val binding: PostBinding) : ViewHolder(binding.root) {
    fun bind(post: Post) {
        val header = binding.header
        val action = binding.action

        binding.contentImage.visibility = View.VISIBLE
        header.monogramText.visibility = View.VISIBLE
        header.username.text = post.author

        header.monogram.load(post.authorAvatar) {
            listener(onSuccess = { _, _ -> header.monogramText.visibility = View.GONE })
        }

        when (post.attachment?.type) {
            AttachmentType.IMAGE -> binding.contentImage.load(post.attachment.url) {
                listener(
                    onSuccess = { _, _ -> binding.contentImage.visibility = View.VISIBLE },
                    onError = { _, _ -> binding.contentImage.visibility = View.GONE }
                )
            }

            AttachmentType.VIDEO -> binding.contentVideo.visibility = View.VISIBLE
            AttachmentType.AUDIO, null -> {
                binding.contentImage.visibility = View.GONE
                binding.contentVideo.visibility = View.GONE
            }
        }

        header.monogramText.text = post.author.firstOrNull()?.toString() ?: ""
        header.datePublished.text = post.published
        binding.contentText.text = post.content

        action.likeButton.isSelected = post.likedByMe
        action.likeButton.text = if (post.likedByMe) "1" else "0"
    }
}