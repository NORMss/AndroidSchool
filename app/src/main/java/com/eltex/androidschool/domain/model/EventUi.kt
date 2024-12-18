package com.eltex.androidschool.domain.model

data class EventUi(
    val id: Long = 0L,
    val content: String = "",
    val author: String = "",
    val published: String = "",
    val datetime: String = "",
    val likedByMe: Boolean = false,
    val participatedByMe: Boolean = false,
    val likes: Int = 0,
    val participants: Int = 0,
)