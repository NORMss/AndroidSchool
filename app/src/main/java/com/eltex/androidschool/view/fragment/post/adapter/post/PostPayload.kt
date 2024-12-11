package com.eltex.androidschool.view.fragment.post.adapter.post

data class PostPayload(
    val likedByMe: Boolean? = null,
    val likeOwnerIds: Set<Long>? = null,
) {
    fun isNotEmpty(): Boolean = likedByMe != null || likeOwnerIds != null
}