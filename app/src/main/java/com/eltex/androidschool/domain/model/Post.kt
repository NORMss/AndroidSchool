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
    @SerialName("mentionIds")
    val mentionIds: Set<Long>,
    @SerialName("mentionedMe")
    val mentionedMe: Boolean,
    @SerialName("likeOwnerIds")
    val likeOwnerIds: Set<Long>,
    @SerialName("likedByMe")
    val likedByMe: Boolean,
    @SerialName("attachment")
    val attachment: Attachment?,
    @SerialName("users")
    val users: Map<String, UserPreview>,
) : DateSeparators.Publishable