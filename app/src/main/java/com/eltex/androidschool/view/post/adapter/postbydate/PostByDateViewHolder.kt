package com.eltex.androidschool.view.post.adapter.postbydate

import android.view.View
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.FragmentPostBinding
import com.eltex.androidschool.domain.model.Post
import com.eltex.androidschool.utils.datatime.DateSeparators
import com.eltex.androidschool.ui.OffsetDecoration
import com.eltex.androidschool.view.post.adapter.post.PostAdapter

class PostByDateViewHolder(
    clickLikeListener: (post: Post) -> Unit,
    clickShareListener: (post: Post) -> Unit,
    clickMoreListener: (post: Post) -> Unit,
    private val binding: FragmentPostBinding,
) : ViewHolder(binding.root) {
    private val viewPool = RecycledViewPool()
    private val postAdapter = PostAdapter(
        clickLikeListener = clickLikeListener,
        clickShareListener = clickShareListener,
        clickMoreListener = clickMoreListener,
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
            text = groupByDate.date
            visibility = View.VISIBLE
        }
    }

    fun bind(postByDatePayload: PostByDatePayload) {
        postByDatePayload.date?.let { date ->
            binding.separator.date.text = date
        }
        postByDatePayload.items?.let { items ->
            postAdapter.submitList(items)
        }
    }
}