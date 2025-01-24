package com.eltex.androidschool.view.fragment.post.adapter.paging

import androidx.recyclerview.widget.DiffUtil
import com.eltex.androidschool.view.fragment.post.adapter.post.PostItemCallback

class PostPagingICallback : DiffUtil.ItemCallback<PagingModel>() {
    private val delegate = PostItemCallback()

    override fun areItemsTheSame(
        oldItem: PagingModel,
        newItem: PagingModel
    ): Boolean {
        if (oldItem::class != newItem::class) {
            return false
        }
        return if (oldItem is PagingModel.Item && newItem is PagingModel.Item) {
            delegate.areItemsTheSame(oldItem.data, newItem.data)
        } else {
            oldItem == newItem
        }
    }

    override fun areContentsTheSame(
        oldItem: PagingModel,
        newItem: PagingModel
    ): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(
        oldItem: PagingModel,
        newItem: PagingModel
    ): Any? {
        if (oldItem::class != newItem::class) {
            return null
        }
        return if (oldItem is PagingModel.Item && newItem is PagingModel.Item) {
            delegate.getChangePayload(oldItem.data, newItem.data)

        } else {
            null
        }
    }
}