package com.eltex.androidschool.view.fragment.editevent

import com.eltex.androidschool.domain.model.Event
import com.eltex.androidschool.domain.model.EventType
import kotlinx.datetime.Instant

data class EditEventState(
    val event: Event = Event(
        id = 0L,
        authorId = 0L,
        author = "",
        authorJob = "",
        authorAvatar = null,
        content = "",
        datetime = Instant.fromEpochSeconds(0),
        published = Instant.fromEpochSeconds(0),
        coords = null,
        type = EventType.OFFLINE,
        likeOwnerIds = emptySet(),
        likedByMe = false,
        speakerIds = emptySet(),
        participantsIds = emptySet(),
        participatedByMe = false,
        attachment = null,
        link = null,
        users = emptyList()
    )
)