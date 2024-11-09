package com.eltex.androidschool.domain.model

data class Event(
    val id: Long,
    val authorId: Long,
    val author: String,
    val authorJob: String?,
    val authorAvatar: String?,
    val content: String,
    val datetime: String,
    val published: String,
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
)