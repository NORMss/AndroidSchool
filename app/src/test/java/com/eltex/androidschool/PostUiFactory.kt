package com.eltex.androidschool

import com.eltex.androidschool.domain.model.Attachment
import com.eltex.androidschool.view.model.PostUi

object PostUiFactory {
    fun create(
        id: Long = 0L,
        content: String = "Test Content",
        author: String = "Test Author",
        authorAvatar: String? = null,
        published: String = "2024-01-01",
        likedByMe: Boolean = false,
        likes: Int = 0,
        attachment: Attachment? = null
    ): PostUi {
        return PostUi(
            id = id,
            content = content,
            author = author,
            authorAvatar = authorAvatar,
            published = published,
            likedByMe = likedByMe,
            likes = likes,
            attachment = attachment
        )
    }
}
