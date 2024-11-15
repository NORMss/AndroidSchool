package com.eltex.androidschool.view.post.adapter

import androidx.recyclerview.widget.DiffUtil
import com.eltex.androidschool.domain.model.Post

class PostItemCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean = oldItem == newItem

    override fun getChangePayload(oldItem: Post, newItem: Post): Any? {
        return PostPayload(
            likedByMe = newItem.likedByMe.takeIf { it != oldItem.likedByMe },
        )
            .takeIf {
                it.isNotEmpty()
            }
    }
}