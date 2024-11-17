package com.eltex.androidschool.view.post.adapter.post

data class PostPayload(
    val likedByMe: Boolean? = null,
) {
    fun isNotEmpty(): Boolean = likedByMe != null
}