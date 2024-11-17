package com.eltex.androidschool.view.post.adapter.postbydate

import com.eltex.androidschool.domain.model.Post

data class PostByDatePayload(
    val date: String? = null,
    val items: List<Post>? = null,
) {
    fun isNotEmpty(): Boolean = date != null || items != null
}