package com.eltex.androidschool.view.fragment.post.adapter.postbydate

import android.view.View
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.PostsByDateBinding
import com.eltex.androidschool.domain.model.Post
import com.eltex.androidschool.utils.datetime.DateSeparators
import com.eltex.androidschool.utils.resourcemanager.AndroidResourceManager
import com.eltex.androidschool.view.common.OffsetDecoration
import com.eltex.androidschool.view.fragment.post.adapter.post.PostAdapter

class PostByDateViewHolder(
    private val postListener: PostAdapter.PostListener,
    private val binding: PostsByDateBinding,
) : ViewHolder(binding.root) {
    private val viewPool = RecycledViewPool()

    private val resourceManager by lazy {
        AndroidResourceManager(
            this.binding.root.context.applicationContext
        )
    }

    private val postAdapter = PostAdapter(
        object : PostAdapter.PostListener {
            override fun onLikeClicked(post: Post) {
                postListener.onLikeClicked(post)
            }

            override fun onShareClicked(post: Post) {
                postListener.onShareClicked(post)
            }

            override fun onMoreClicked(post: Post, view: View) {
                postListener.onMoreClicked(post, view)
            }
        }
    )

    init {
        binding.posts.setRecycledViewPool(viewPool)
        binding.posts.adapter = postAdapter
        binding.posts.addItemDecoration(
            OffsetDecoration(
                horizontalOffset = binding.posts.resources.getDimensionPixelSize(R.dimen.zero),
                verticalOffset = binding.posts.resources.getDimensionPixelSize(R.dimen.list_offset)
            )
        )
    }

    fun bind(groupByDate: DateSeparators.GroupByDate<Post>) {
        postAdapter.submitList(groupByDate.items)
        binding.separator.date.apply {
            text = DateSeparators.formatInstantToString(
                instant = groupByDate.date,
                resourceManager = resourceManager
            )
            visibility = View.VISIBLE
        }
    }

    fun bind(postByDatePayload: PostByDatePayload) {
        postByDatePayload.date?.let { date ->
            binding.separator.date.text = DateSeparators.formatInstantToString(
                instant = date,
                resourceManager = resourceManager
            )
        }
        postByDatePayload.items?.let { items ->
            postAdapter.submitList(items)
        }
    }
}