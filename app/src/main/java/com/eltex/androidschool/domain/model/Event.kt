package com.eltex.androidschool.domain.model

import com.eltex.androidschool.view.util.datetime.DateSeparators
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Event(
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
    @SerialName("datetime")
    val datetime: Instant,
    @SerialName("published")
    override val published: Instant,
    @SerialName("coords")
    val coords: Coordinates?,
    @SerialName("type")
    val type: EventType,
    @SerialName("likeOwnerIds")
    val likeOwnerIds: Set<Long>,
    @SerialName("likedByMe")
    val likedByMe: Boolean,
    @SerialName("speakerIds")
    val speakerIds: Set<Long>,
    @SerialName("participantsIds")
    val participantsIds: Set<Long>,
    @SerialName("participatedByMe")
    val participatedByMe: Boolean,
    @SerialName("attachment")
    val attachment: Attachment?,
    @SerialName("link")
    val link: String?,
    @SerialName("users")
    val users: Map<String, UserPreview>,
) : DateSeparators.Publishable