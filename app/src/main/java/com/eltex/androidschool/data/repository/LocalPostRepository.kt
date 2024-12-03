package com.eltex.androidschool.data.repository

import com.eltex.androidschool.domain.local.LocalPostsManager
import com.eltex.androidschool.domain.model.Attachment
import com.eltex.androidschool.domain.model.Coordinates
import com.eltex.androidschool.domain.model.Post
import com.eltex.androidschool.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock

class LocalPostRepository(
    private val localPostsManager: LocalPostsManager,
) : PostRepository {
    override fun getPosts(): Flow<List<Post>> =
        localPostsManager.getPosts()

    override suspend fun likeById(id: Long) {
        localPostsManager.updatePost(id) {
            it.copy(likedByMe = !it.likedByMe)
        }
    }

    override suspend fun getPostById(id: Long): Post {
        return localPostsManager.getPosts().first().filter {
            it.id == id
        }.first()
    }

    override suspend fun deletePostById(id: Long) {
        localPostsManager.deletePost(id)
    }

    override suspend fun addPost(textContent: String, attachment: Attachment?) {
        localPostsManager.addPost(
            Post(
                id = localPostsManager.generateNextId(),
                authorId = 1000,
                author = "Sergey Bezborodov",
                authorJob = "Junior Android Developer",
                authorAvatar = "https://avatars.githubusercontent.com/u/47896309?v=4",
                content = textContent,
                published = Clock.System.now(),
                coordinates = Coordinates(
                    lat = 54.9833,
                    long = 82.8964,
                ),
                link = "https://github.com/NORMss/",
                mentionedMe = false,
                likedByMe = false,
                attachment = attachment
            ),
        )
    }

    override suspend fun editPostById(id: Long, textContent: String) {
        localPostsManager.updatePost(id) { post ->
            post.copy(content = textContent)
        }
    }
}