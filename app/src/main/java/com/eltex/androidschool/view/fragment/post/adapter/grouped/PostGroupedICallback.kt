package com.eltex.androidschool.view.fragment.post.adapter.grouped

import androidx.recyclerview.widget.DiffUtil
import com.eltex.androidschool.view.fragment.post.adapter.post.PostItemCallback

class PostGroupedICallback : DiffUtil.ItemCallback<PostListItem>() {
    private val delegate = PostItemCallback()

    override fun areItemsTheSame(
        oldItem: PostListItem,
        newItem: PostListItem
    ): Boolean {
        if (oldItem::class != newItem::class) {
            return false
        }
        return if (oldItem is PostListItem.ItemPost && newItem is PostListItem.ItemPost) {
            delegate.areItemsTheSame(oldItem.data, newItem.data)
        } else {
            oldItem == newItem
        }
    }

    override fun areContentsTheSame(
        oldItem: PostListItem,
        newItem: PostListItem
    ): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: PostListItem, newItem: PostListItem): Any? {
        if (oldItem::class != newItem::class) {
            return null
        }
        return if (oldItem is PostListItem.ItemPost && newItem is PostListItem.ItemPost) {
            delegate.getChangePayload(oldItem.data, newItem.data)

        } else {
            null
        }
    }
}