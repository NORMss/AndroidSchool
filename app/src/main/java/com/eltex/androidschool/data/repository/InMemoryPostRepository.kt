package com.eltex.androidschool.data.repository

import com.eltex.androidschool.domain.local.LocalPostsManager
import com.eltex.androidschool.domain.model.Attachment
import com.eltex.androidschool.domain.model.AttachmentType
import com.eltex.androidschool.domain.model.Coordinates
import com.eltex.androidschool.domain.model.Post
import com.eltex.androidschool.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.Clock

class InMemoryPostRepository(
    private val localPostsManager: LocalPostsManager,
) : PostRepository {
    private val _state = MutableStateFlow(emptyList<Post>())

    override fun getPost(): Flow<List<Post>> =
        _state.asStateFlow()

    override fun likeById(id: Long) {
        _state.update { posts ->
            posts.map {
                if (it.id == id) {
                    it.copy(
                        likedByMe = !it.likedByMe
                    )
                } else {
                    it
                }
            }
        }
    }

    override suspend fun deletePostById(id: Long) {
        localPostsManager.deletePost(id)
    }

    override suspend fun addPost(textContent: String, imageContent: String?) {
        localPostsManager.savePost(
            Post(
                id = localPostsManager.generateNextId(),
                authorId = localPostsManager.generateNextId(),
                author = "Sergey Bezborodov",
                authorJob = "Junior Android Developer",
                authorAvatar = "https://avatars.githubusercontent.com/u/47896309?v=4",
                content = textContent,
                published = Clock.System.now().toString(),
                coordinates = Coordinates(
                    lat = 54.9833,
                    long = 82.8964,
                ),
                link = "https://github.com/NORMss/",
                mentionedMe = false,
                likedByMe = false,
                attachment = imageContent?.let { Attachment(it, AttachmentType.IMAGE) }
            ),
        )
    }

    override suspend fun editPostById(id: Long, textContent: String) {
        localPostsManager.updatePost(
            TODO()
        )
    }
}