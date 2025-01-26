package com.eltex.androidschool.view.fragment.post.adapter.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.ItemErrorBinding
import com.eltex.androidschool.databinding.PostBinding
import com.eltex.androidschool.databinding.PostSkeletonBinding
import com.eltex.androidschool.databinding.SeparatorDateBinding
import com.eltex.androidschool.view.fragment.common.ErrorViewHolder
import com.eltex.androidschool.view.fragment.common.SeparatorDateViewHolder
import com.eltex.androidschool.view.fragment.post.adapter.post.PostAdapter
import com.eltex.androidschool.view.fragment.post.adapter.post.PostPayload
import com.eltex.androidschool.view.fragment.post.adapter.post.PostViewHolder

class PostPagingAdapter(
    private val postListener: PostAdapter.PostListener,
) : ListAdapter<PostPagingModel, RecyclerView.ViewHolder>(PostPagingICallback()) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is PostPagingModel.Header -> R.layout.separator_date
            is PostPagingModel.Item -> R.layout.post
            is PostPagingModel.Error -> R.layout.item_error
            PostPagingModel.Loading -> R.layout.post_skeleton
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val postBinding = PostBinding.inflate(layoutInflater, parent, false)
        val separatorDateBinding = SeparatorDateBinding.inflate(layoutInflater, parent, false)
        val itemErrorBinding = ItemErrorBinding.inflate(layoutInflater, parent, false)
        val progressBinding = PostSkeletonBinding.inflate(layoutInflater, parent, false)

        return when (viewType) {
            R.layout.separator_date -> {
                SeparatorDateViewHolder(separatorDateBinding)
            }

            R.layout.post -> {
                val postViewHolder = PostViewHolder(postBinding)
                setupClickListenersByPost(postBinding, postViewHolder)
                postViewHolder
            }

            R.layout.item_error -> {
                val errorViewHolder = ErrorViewHolder(itemErrorBinding)
                itemErrorBinding.retry.setOnClickListener {
                    postListener.onLoadNextPage()
                }
                errorViewHolder
            }

            R.layout.post_skeleton -> {
                PostProgressViewHolder(progressBinding)
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
            is PostPagingModel.Header -> {
                (holder as SeparatorDateViewHolder).bind(item.date)
            }

            is PostPagingModel.Item -> {
                (holder as PostViewHolder).bind(item.data)
            }

            is PostPagingModel.Error -> {
                (holder as ErrorViewHolder).bind(item.reason)
            }

            PostPagingModel.Loading -> Unit
        }
    }

    private fun setupClickListenersByPost(binding: PostBinding, viewHolder: PostViewHolder) {
        binding.likeButton.setOnClickListener {
            val item = getItem(viewHolder.adapterPosition) as? PostPagingModel.Item
            item?.data?.let(postListener::onLikeClicked)
        }
        binding.shareButton.setOnClickListener {
            val item = getItem(viewHolder.adapterPosition) as? PostPagingModel.Item
            item?.data?.let(postListener::onShareClicked)
        }
        binding.header.moreButton.setOnClickListener { view ->
            val item = getItem(viewHolder.adapterPosition) as? PostPagingModel.Item
            item?.let { item ->
                postListener.onMoreClicked(
                    item.data,
                    view,
                )
            }
        }
    }
}