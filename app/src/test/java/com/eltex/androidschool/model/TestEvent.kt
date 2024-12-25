package com.eltex.androidschool.model

import com.eltex.androidschool.domain.model.Event
import com.eltex.androidschool.domain.model.EventType
import kotlinx.datetime.Instant

data class TestEvent(
    val id: Long,
    val author: String = "Test Author",
    val content: String = "Test Content",
    val published: Instant = Instant.fromEpochMilliseconds(0),
    val datetime: Instant = Instant.fromEpochMilliseconds(0),
    val likedByMe: Boolean = false,
    val participatedByMe: Boolean = false,
) {
    fun toDomainEvent(): Event = Event(
        id = id,
        authorId = 1L,
        author = author,
        authorJob = null,
        authorAvatar = null,
        content = content,
        datetime = datetime,
        published = published,
        coords = null,
        type = EventType.ONLINE,
        likeOwnerIds = if (likedByMe) setOf(1L) else emptySet(),
        likedByMe = likedByMe,
        speakerIds = emptySet(),
        participantsIds = if (participatedByMe) setOf(1L) else emptySet(),
        participatedByMe = participatedByMe,
        attachment = null,
        link = null,
        users = emptyMap(),
    )
}
