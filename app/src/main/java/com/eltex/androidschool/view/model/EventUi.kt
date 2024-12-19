package com.eltex.androidschool.view.model

import com.eltex.androidschool.domain.model.Attachment
import com.eltex.androidschool.domain.model.EventType

data class EventUi(
    val id: Long = 0L,
    val content: String = "",
    val author: String = "",
    val authorAvatar: String? = null,
    val published: String = "",
    val datetime: String = "",
    val likedByMe: Boolean = false,
    val participatedByMe: Boolean = false,
    val likes: Int = 0,
    val participants: Int = 0,
    val attachment: Attachment? = null,
    val type: EventType,
    val link: String? = null,
)