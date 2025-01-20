package com.eltex.androidschool.view.fragment.post.adapter.grouped

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eltex.androidschool.databinding.PostBinding
import com.eltex.androidschool.view.fragment.post.adapter.post.PostAdapter
import com.eltex.androidschool.view.fragment.post.adapter.post.PostViewHolder

class GroupedAdapter(
    private val itemList: List<ListItem>,
    private val postListener: PostAdapter.PostListener,
) : ListAdapter<ListItem, RecyclerView.ViewHolder>(GroupedICallback()) {
    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_ITEM = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (itemList[position]) {
            is ListItem.Header -> TYPE_HEADER
            is ListItem.Item -> TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PostViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = PostBinding.inflate(layoutInflater, parent, false)


        return when (viewType) {
            TYPE_HEADER -> {
                val postViewHolder = PostViewHolder(binding)
                binding.shareButton.setOnClickListener {
                    val item = getItem(postViewHolder.adapterPosition) as? ListItem.Item
                    item?.postUI?.let {
                        postListener.onShareClicked(it)
                    }
                }
                binding.likeButton.setOnClickListener {
                    val item = getItem(postViewHolder.adapterPosition) as? ListItem.Item
                    item?.postUI?.let {
                        postListener.onLikeClicked(it)
                    }
                }
                postViewHolder
            }

            TYPE_ITEM -> {
                TODO()
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}