package com.eltex.androidschool.view.model

import com.eltex.androidschool.domain.model.Attachment
import com.eltex.androidschool.view.util.datetime.DateSeparators
import kotlinx.datetime.Instant

data class PostUi(
    val id: Long = 0L,
    val content: String = "",
    val author: String = "",
    val authorAvatar: String? = null,
    override val published: Instant = Instant.fromEpochSeconds(0),
    val likedByMe: Boolean = false,
    val likes: Int = 0,
    val attachment: Attachment? = null,
) : DateSeparators.Publishable
