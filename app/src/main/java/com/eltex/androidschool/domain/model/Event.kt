package com.eltex.androidschool.domain.model

import com.eltex.androidschool.utils.datetime.DateSeparators
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class Event(
    val id: Long,
    val authorId: Long,
    val author: String,
    val authorJob: String?,
    val authorAvatar: String?,
    val content: String,
    val datetime: Instant,
    override val published: Instant,
    val coords: Coordinates?,
    val type: EventType,
    val likeOwnerIds: Set<Long>,
    val likedByMe: Boolean,
    val speakerIds: Set<Long>,
    val participantsIds: Set<Long>,
    val participatedByMe: Boolean,
    val attachment: Attachment?,
    val link: String?,
    val users: List<UserPreview>
) : DateSeparators.Publishable