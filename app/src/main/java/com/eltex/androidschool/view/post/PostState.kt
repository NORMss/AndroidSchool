package com.eltex.androidschool.view.post

import com.eltex.androidschool.domain.model.Post
import kotlinx.coroutines.flow.Flow

data class PostState(
    val post: Post? = null,
    val toast: Pair<Int, Boolean>? = null
)