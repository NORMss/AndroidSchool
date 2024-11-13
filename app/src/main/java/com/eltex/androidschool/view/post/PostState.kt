package com.eltex.androidschool.view.post

import com.eltex.androidschool.domain.model.Post

data class PostState(
    val post: List<Post> = emptyList(),
    val toast: Pair<Int, Boolean>? = null,
)