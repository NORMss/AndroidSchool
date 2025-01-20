package com.eltex.androidschool.view.fragment.post.adapter.grouped

import androidx.recyclerview.widget.DiffUtil
import com.eltex.androidschool.view.fragment.post.adapter.post.PostItemCallback

class GroupedICallback : DiffUtil.ItemCallback<ListItem>() {
    private val delegate = PostItemCallback()

    override fun areItemsTheSame(
        oldItem: ListItem,
        newItem: ListItem
    ): Boolean {
        if (oldItem::class == newItem::class) {
            return false
        }
        return if (oldItem is ListItem.Item && newItem is ListItem.Item) {
            delegate.areItemsTheSame(oldItem.postUI, newItem.postUI)
        } else {
            oldItem == newItem
        }
    }

    override fun areContentsTheSame(
        oldItem: ListItem,
        newItem: ListItem
    ): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: ListItem, newItem: ListItem): Any? {
        if (oldItem::class == newItem::class) {
            return null
        }
        return if (oldItem is ListItem.Item && newItem is ListItem.Item) {
            delegate.getChangePayload(oldItem.postUI, newItem.postUI)

        } else {
            null
        }
    }
}