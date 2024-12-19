package com.eltex.androidschool.view.model

import com.eltex.androidschool.domain.model.Attachment

data class PostUi(
    val id: Long = 0L,
    val content: String = "",
    val author: String = "",
    val authorAvatar: String? = null,
    val published: String = "",
    val likedByMe: Boolean = false,
    val likes: Int = 0,
    val attachment: Attachment? = null,
)
