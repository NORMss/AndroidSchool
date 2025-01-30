package com.eltex.androidschool.view.mapper

import com.eltex.androidschool.domain.mapper.Mapper
import com.eltex.androidschool.domain.model.Post
import com.eltex.androidschool.view.model.PostUi
import javax.inject.Inject

class PostUiMapper @Inject constructor() : Mapper<Post, PostUi> {
    override fun map(post: Post): PostUi = with(post) {
        PostUi(
            id = id,
            content = content,
            author = author,
            published = published,
            likedByMe = likedByMe,
            likes = likeOwnerIds.size,
            authorAvatar = authorAvatar,
            attachment = attachment,
        )
    }
}