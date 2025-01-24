package com.eltex.androidschool.view.fragment.post.adapter.post

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.eltex.androidschool.databinding.PostBinding
import com.eltex.androidschool.view.model.PostUi

class PostAdapter(
    private val postListener: PostListener,
) : ListAdapter<PostUi, PostViewHolder>(
    PostItemCallback()
) {
    interface PostListener {
        fun onLikeClicked(post: PostUi)
        fun onLoadNextPage()
        fun onShareClicked(post: PostUi)
        fun onMoreClicked(post: PostUi, view: View)
    }

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
        binding.likeButton.setOnClickListener { postListener.onLikeClicked(getItem(viewHolder.adapterPosition)) }
        binding.shareButton.setOnClickListener { postListener.onShareClicked(getItem(viewHolder.adapterPosition)) }
        binding.header.moreButton.setOnClickListener {
            postListener.onMoreClicked(
                getItem(
                    viewHolder.adapterPosition
                ), it
            )
        }
    }
}