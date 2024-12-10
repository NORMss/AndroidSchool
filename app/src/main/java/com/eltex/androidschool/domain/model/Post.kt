package com.eltex.androidschool.domain.model

import com.eltex.androidschool.utils.datetime.DateSeparators
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Post(
    @SerialName("id")
    val id: Long,
    @SerialName("authorId")
    val authorId: Long,
    @SerialName("author")
    val author: String,
    @SerialName("authorJob")
    val authorJob: String?,
    @SerialName("authorAvatar")
    val authorAvatar: String?,
    @SerialName("content")
    val content: String,
    @SerialName("published")
    override val published: Instant,
    @SerialName("coords")
    val coords: Coordinates?,
    @SerialName("link")
    val link: String?,
    @SerialName("mentionedMe")
    val mentionedMe: Boolean,
    @SerialName("likedByMe")
    val likedByMe: Boolean,
    @SerialName("attachment")
    val attachment: Attachment?,
) : DateSeparators.Publishable