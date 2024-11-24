package com.eltex.androidschool.domain.model

import com.eltex.androidschool.utils.datetime.DateSeparators
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val id: Long,
    val authorId: Long,
    val author: String,
    val authorJob: String,
    val authorAvatar: String?,
    val content: String,
    override val published: Instant,
    val coordinates: Coordinates?,
    val link: String?,
    val mentionedMe: Boolean,
    val likedByMe: Boolean,
    val attachment: Attachment?,
) : DateSeparators.Publishable