package com.eltex.androidschool.view.fragment.post.adapter.grouped

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.PostBinding
import com.eltex.androidschool.databinding.SeparatorDateBinding
import com.eltex.androidschool.view.fragment.post.adapter.post.PostAdapter
import com.eltex.androidschool.view.fragment.post.adapter.post.PostPayload
import com.eltex.androidschool.view.fragment.post.adapter.post.PostViewHolder

class PostGroupedAdapter(
    private val postListener: PostAdapter.PostListener,
) : ListAdapter<PostListItem, RecyclerView.ViewHolder>(PostGroupedICallback()) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is PostListItem.Header -> R.layout.separator_date
            is PostListItem.ItemPost -> R.layout.post
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val postBinding = PostBinding.inflate(layoutInflater, parent, false)
        val separatorDateBinding = SeparatorDateBinding.inflate(layoutInflater, parent, false)

        return when (viewType) {
            R.layout.separator_date -> {
                SeparatorDateViewHolder(separatorDateBinding)
            }

            R.layout.post -> {
                val postViewHolder = PostViewHolder(postBinding)
                setupClickListenersByPost(postBinding, postViewHolder)
                postViewHolder
            }

            else -> error("Unknown viewType: $viewType")
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: List<Any?>
    ) {
        if (payloads.isEmpty())
            onBindViewHolder(holder, position)
        else
            payloads.forEach {
                if (it is PostPayload) {
                    (holder as? PostViewHolder)?.bind(it)
                }
            }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        when (val item = getItem(position)) {
            is PostListItem.Header -> {
                (holder as SeparatorDateViewHolder).bind(item.date)
            }

            is PostListItem.ItemPost -> {
                (holder as PostViewHolder).bind(item.data)
            }
        }
    }

    private fun setupClickListenersByPost(binding: PostBinding, viewHolder: PostViewHolder) {
        binding.likeButton.setOnClickListener {
            val item = getItem(viewHolder.adapterPosition) as? PostListItem.ItemPost
            item?.data?.let(postListener::onLikeClicked)
        }
        binding.shareButton.setOnClickListener {
            val item = getItem(viewHolder.adapterPosition) as? PostListItem.ItemPost
            item?.data?.let(postListener::onShareClicked)
        }
        binding.header.moreButton.setOnClickListener { view ->
            val item = getItem(viewHolder.adapterPosition) as? PostListItem.ItemPost
            item?.let { item ->
                postListener.onMoreClicked(
                    item.data,
                    view,
                )
            }
        }
    }
}