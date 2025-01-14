package com.eltex.androidschool.data.repository

import com.eltex.androidschool.data.remote.api.PostApi
import com.eltex.androidschool.domain.model.Post
import com.eltex.androidschool.domain.repository.PostRepository

class RemotePostRepository(
    private val postApi: PostApi
) : PostRepository {
    override suspend fun getPosts(): List<Post> {
        return postApi.getPosts()
    }

    override suspend fun getPostsNewer(id: Long): List<Post> {
        return postApi.getPostsNewer(id)
    }

    override suspend fun getPostsBefore(
        id: Long,
        count: Int
    ): List<Post> {
        return postApi.getPostsBefore(id, count)
    }

    override suspend fun getPostsAfter(
        id: Long,
        count: Int
    ): List<Post> {
        return postApi.getPostsAfter(id, count)
    }

    override suspend fun getPostsLatest(count: Int): List<Post> {
        return postApi.getPostsLatest(count)
    }

    override suspend fun likeById(
        id: Long,
        isLiked: Boolean
    ): Post {
        return when (isLiked) {
            true -> postApi.unlikeById(id)
            false -> postApi.likeById(id)
        }
    }

    override suspend fun savePost(post: Post): Post {
        return postApi.save(post)
    }

    override suspend fun deleteById(id: Long) {
        return postApi.deleteById(id)
    }
}