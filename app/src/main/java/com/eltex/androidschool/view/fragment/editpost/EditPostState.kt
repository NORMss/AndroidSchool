package com.eltex.androidschool.view.fragment.editpost

import com.eltex.androidschool.domain.model.Post
import kotlinx.datetime.Instant

data class EditPostState(
    val post: Post = Post(
        id = 0L,
        authorId = 0L,
        author = "",
        authorJob = "",
        authorAvatar = null,
        content = "",
        published = Instant.fromEpochSeconds(0L),
        coordinates = null,
        link = null,
        mentionedMe = false,
        likedByMe = false,
        attachment = null,
    )
)