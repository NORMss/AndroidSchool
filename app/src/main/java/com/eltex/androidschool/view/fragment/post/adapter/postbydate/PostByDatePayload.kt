package com.eltex.androidschool.view.fragment.post.adapter.postbydate

import com.eltex.androidschool.domain.model.Post
import kotlinx.datetime.Instant

data class PostByDatePayload(
    val date: Instant? = null,
    val items: List<Post>? = null,
) {
    fun isNotEmpty(): Boolean = date != null || items != null
}