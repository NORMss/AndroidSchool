package com.eltex.androidschool.view.post.adapter

data class PostByDatePayload(
    val date: String? = null
) {
    fun isNotEmpty(): Boolean = date != null
}