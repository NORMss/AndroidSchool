package com.eltex.androidschool.view.mapper

import com.eltex.androidschool.domain.mapper.Mapper
import com.eltex.androidschool.domain.model.Post
import com.eltex.androidschool.view.model.PostUi
import com.eltex.androidschool.view.util.datetime.DateTimeStringFormatter
import javax.inject.Inject

class PostUiMapper @Inject constructor(
    private val dateTimeStringFormatter: DateTimeStringFormatter,
) : Mapper<Post, PostUi> {
    override fun map(post: Post): PostUi = with(post) {
        PostUi(
            id = id,
            content = content,
            author = author,
            formattedPublished = dateTimeStringFormatter.format(published),
            published = published,
            likedByMe = likedByMe,
            likes = likeOwnerIds.size,
            authorAvatar = authorAvatar,
            attachment = attachment,
        )
    }
}