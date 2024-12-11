package com.eltex.androidschool.view.fragment.editpost

import com.eltex.androidschool.domain.model.Post
import com.eltex.androidschool.view.common.Status
import kotlinx.datetime.Instant

data class EditPostState(
    val post: Post = Post(
        id = 0L,
        authorId = 0L,
        author = "",
        authorJob = null,
        authorAvatar = null,
        content = "",
        published = Instant.fromEpochSeconds(0L),
        coords = null,
        link = null,
        mentionIds = emptySet(),
        mentionedMe = false,
        likeOwnerIds = emptySet(),
        likedByMe = false,
        attachment = null,
        users = emptyMap()
    ),
    val status: Status = Status.Idle,
) {
    val isRefreshing: Boolean
        get() = status == Status.Loading && post.content.isNotBlank()
    val isEmptyLoading: Boolean
        get() = status == Status.Loading && post.content.isBlank()
    val isEmptyError: Boolean
        get() = status is Status.Error && post.content.isNotBlank()
    val isRefreshError: Boolean
        get() = status is Status.Error && post.content.isNotBlank()
}