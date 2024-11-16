package com.eltex.androidschool.view.post.adapter

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.eltex.androidschool.databinding.FragmentPostBinding
import com.eltex.androidschool.domain.model.Post
import com.eltex.androidschool.utils.datatime.DateSeparators

class PostByDateViewHolder(
    private val binding: FragmentPostBinding,
) : ViewHolder(binding.root) {
    private val viewPool = RecycledViewPool()
    private val postAdapter = PostAdapter(
        clickLikeListener = {},
        clickShareListener = {},
        clickMoreListener = {}
    )

    init {
        binding.posts.layoutManager = LinearLayoutManager(binding.posts.context).apply {
            orientation = LinearLayoutManager.VERTICAL
        }
        binding.posts.setRecycledViewPool(viewPool)
        binding.posts.adapter = postAdapter
    }

    fun bind(groupByDate: DateSeparators.GroupByDate<Post>) {
        postAdapter.submitList(groupByDate.items)
        binding.date.text = groupByDate.date
    }

    fun bind(postByDatePayload: PostByDatePayload) {
        postByDatePayload.date?.let { date ->
            binding.date.text = date
        }
    }
}