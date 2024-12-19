package com.eltex.androidschool.view.fragment.post.adapter.post

import androidx.recyclerview.widget.DiffUtil.ItemCallback
import com.eltex.androidschool.view.model.PostUi

class PostItemCallback : ItemCallback<PostUi>() {
    override fun areItemsTheSame(oldItem: PostUi, newItem: PostUi): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: PostUi, newItem: PostUi): Boolean = oldItem == newItem

    override fun getChangePayload(oldItem: PostUi, newItem: PostUi): Any? {
        return PostPayload(
            likedByMe = newItem.likedByMe.takeIf { it != oldItem.likedByMe },
            likes = newItem.likes.takeIf { it != oldItem.likes }
        )
            .takeIf {
                it.isNotEmpty()
            }
    }
}