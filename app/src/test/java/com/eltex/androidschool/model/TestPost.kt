package com.eltex.androidschool.model

import com.eltex.androidschool.domain.model.Post
import kotlinx.datetime.Instant

data class TestPost(
    val id: Long,
    val author: String = "Test Author",
    val content: String = "Test Content",
    val published: Instant = Instant.fromEpochMilliseconds(0),
    val likedByMe: Boolean = false
) {
    fun toDomainPost(): Post = Post(
        id = id,
        authorId = 1L,
        author = author,
        authorJob = null,
        authorAvatar = null,
        content = content,
        published = published,
        coords = null,
        link = null,
        mentionIds = emptySet(),
        mentionedMe = false,
        likeOwnerIds = if (likedByMe) setOf(1L) else emptySet(),
        likedByMe = likedByMe,
        attachment = null,
        users = emptyMap()
    )
}
