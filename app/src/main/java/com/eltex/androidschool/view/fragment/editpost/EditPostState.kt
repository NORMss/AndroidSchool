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
        mentionedMe = false,
        likedByMe = false,
        attachment = null,
    ),
    val status: Status = Status.Idle,
) {
    val isRefreshing: Boolean
        get() = status == Status.Loading && post.content.isBlank()
    val isEmptyLoading: Boolean
        get() = status == Status.Loading && post.content.isBlank()
    val isEmptyError: Boolean
        get() = status is Status.Error && post.content.isNotBlank() == true
    val isRefreshingError: Boolean
        get() = status is Status.Error && post.content.isBlank()
}