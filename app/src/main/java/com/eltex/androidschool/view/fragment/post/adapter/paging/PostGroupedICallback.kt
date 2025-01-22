package com.eltex.androidschool.view.fragment.post.adapter.paging

import androidx.recyclerview.widget.DiffUtil
import com.eltex.androidschool.view.fragment.post.adapter.post.PostItemCallback

class PostGroupedICallback : DiffUtil.ItemCallback<PostPagingModel>() {
    private val delegate = PostItemCallback()

    override fun areItemsTheSame(
        oldItem: PostPagingModel,
        newItem: PostPagingModel
    ): Boolean {
        if (oldItem::class != newItem::class) {
            return false
        }
        return if (oldItem is PostPagingModel.ItemPost && newItem is PostPagingModel.ItemPost) {
            delegate.areItemsTheSame(oldItem.data, newItem.data)
        } else {
            oldItem == newItem
        }
    }

    override fun areContentsTheSame(
        oldItem: PostPagingModel,
        newItem: PostPagingModel
    ): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: PostPagingModel, newItem: PostPagingModel): Any? {
        if (oldItem::class != newItem::class) {
            return null
        }
        return if (oldItem is PostPagingModel.ItemPost && newItem is PostPagingModel.ItemPost) {
            delegate.getChangePayload(oldItem.data, newItem.data)

        } else {
            null
        }
    }
}