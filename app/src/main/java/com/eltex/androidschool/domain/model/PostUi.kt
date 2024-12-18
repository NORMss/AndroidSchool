package com.eltex.androidschool.domain.model

data class PostUi(
    val id: Long = 0L,
    val content: String = "",
    val author: String = "",
    val published: String = "",
    val likedByMe: Boolean = false,
    val likes: Int = 0,
)
