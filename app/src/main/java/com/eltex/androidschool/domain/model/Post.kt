package com.eltex.androidschool.domain.model

import com.eltex.androidschool.utils.datatime.DateSeparators
import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val id: Long,
    val authorId: Long,
    val author: String,
    val authorJob: String,
    val authorAvatar: String?,
    val content: String,
    override val published: String,
    val coordinates: Coordinates?,
    val link: String?,
    val mentionedMe: Boolean,
    val likedByMe: Boolean,
    val attachment: Attachment?,
) : DateSeparators.Publishable