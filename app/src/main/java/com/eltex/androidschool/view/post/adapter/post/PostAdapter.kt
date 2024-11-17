package com.eltex.androidschool.view.post.adapter.post

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.eltex.androidschool.databinding.PostBinding
import com.eltex.androidschool.domain.model.Post

class PostAdapter(
    private val clickLikeListener: (post: Post) -> Unit,
    private val clickShareListener: (post: Post) -> Unit,
    private val clickMoreListener: (post: Post) -> Unit,
) : ListAdapter<Post, PostViewHolder>(
    PostItemCallback()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = PostBinding.inflate(layoutInflater, parent, false)
        val viewHolder = PostViewHolder(binding)

        setupClickListeners(binding, viewHolder)

        return viewHolder
    }

    override fun onBindViewHolder(
        holder: PostViewHolder,
        position: Int,
        payloads: List<Any>
    ) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            payloads.forEach {
                if (it is PostPayload) {
                    holder.bind(it)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private fun setupClickListeners(binding: PostBinding, viewHolder: PostViewHolder) {
        binding.action.likeButton.setOnClickListener { clickLikeListener(getItem(viewHolder.adapterPosition)) }
        binding.action.shareButton.setOnClickListener { clickShareListener(getItem(viewHolder.adapterPosition)) }
        binding.header.moreButton.setOnClickListener { clickMoreListener(getItem(viewHolder.adapterPosition)) }
    }
}