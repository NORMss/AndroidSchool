package com.eltex.androidschool.data.repository

import com.eltex.androidschool.data.local.post.PostDao
import com.eltex.androidschool.data.local.post.entity.PostEntity
import com.eltex.androidschool.domain.model.Attachment
import com.eltex.androidschool.domain.model.Coordinates
import com.eltex.androidschool.domain.model.Post
import com.eltex.androidschool.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock

class RoomPostRepository(
    private val postDao: PostDao
) : PostRepository {

    override fun getPosts(): Flow<List<Post>> =
        postDao.getPosts().map { it.map(PostEntity::toPost) }

    override suspend fun likeById(id: Long) {
        val post = postDao.getPost(id)
        post?.copy(likedByMe = !post.likedByMe)?.let {
            postDao.savePost(it)
        }
    }

    override suspend fun deletePostById(id: Long) {
        postDao.deletePost(id)
    }

    override suspend fun editPostById(id: Long, textContent: String) {
        val post = postDao.getPost(id)
        post?.copy(content = textContent)?.let {
            postDao.savePost(it)
        }
    }

    override suspend fun addPost(textContent: String, attachment: Attachment?) {
        val newPost = Post(
            id = 0,
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
        )
        postDao.savePost(PostEntity.fromPost(newPost))
    }
}