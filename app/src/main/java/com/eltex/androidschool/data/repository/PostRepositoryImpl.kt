package com.eltex.androidschool.data.repository

import com.eltex.androidschool.domain.model.Attachment
import com.eltex.androidschool.domain.model.AttachmentType
import com.eltex.androidschool.domain.model.Coordinates
import com.eltex.androidschool.domain.model.Post
import com.eltex.androidschool.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PostRepositoryImpl : PostRepository {
    override fun getPost(): Flow<Post> {
        return flow {
            Post(
                id = 1L,
                authorId = 1001L,
                author = "Sergey Bezborodov",
                authorJob = "Android Developer",
                authorAvatar = "https://avatars.githubusercontent.com/u/47896309?v=4",
                content = "Сегодня поделюсь своим опытом работы с Jetpack Compose!",
                published = "2024-11-05T14:30:00",
                coordinates = Coordinates(lat = 55.7558, long = 37.6176),
                link = "https://example.com/article",
                mentionedMe = true,
                likedByMe = false,
                attachment = Attachment(
                    url = "https://static1.xdaimages.com/wordpress/wp-content/uploads/2021/02/Jetpack-Compose-Beta.jpg?q=50&fit=crop&w=1100&h=618&dpr=1.5",
                    type = AttachmentType.IMAGE,
                )
            )
        }
    }

    override fun like() {
        TODO("Not yet implemented")
    }
}