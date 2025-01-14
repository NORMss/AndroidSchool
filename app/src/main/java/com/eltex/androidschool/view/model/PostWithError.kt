package com.eltex.androidschool.view.model

data class PostWithError(
    val post: PostUi,
    val error: Throwable,
)