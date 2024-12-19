package com.eltex.androidschool.view.fragment.post.adapter.postbydate

import com.eltex.androidschool.view.model.PostUi
import kotlinx.datetime.Instant

data class PostByDatePayload(
    val date: Instant? = null,
    val items: List<PostUi>? = null,
) {
    fun isNotEmpty(): Boolean = date != null || items != null
}