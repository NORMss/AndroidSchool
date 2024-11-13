package com.eltex.androidschool.view.post.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eltex.androidschool.databinding.PostBinding
import com.eltex.androidschool.domain.model.Post

class PostAdapter(
    private val clickLikeListener: (post: Post) -> Unit,
    private val clickShareListener: (post: Post) -> Unit,
    private val clickMoreListener: (post: Post) -> Unit,
) : RecyclerView.Adapter<PostViewHolder>() {
    val posts: List<Post> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = PostBinding.inflate(layoutInflater, parent, false)
        val viewHolder = PostViewHolder(binding)

        setupClickListeners(binding, viewHolder)

        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(posts[position])
    }

    override fun getItemCount(): Int = posts.size

    private fun setupClickListeners(binding: PostBinding, viewHolder: PostViewHolder) {
        binding.action.likeButton.setOnClickListener { clickLikeListener(posts[viewHolder.adapterPosition]) }
        binding.action.shareButton.setOnClickListener { clickShareListener(posts[viewHolder.adapterPosition]) }
        binding.header.moreButton.setOnClickListener { clickMoreListener(posts[viewHolder.adapterPosition]) }
    }
}