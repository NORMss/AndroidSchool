package com.eltex.androidschool.repository

import com.eltex.androidschool.domain.model.Post
import com.eltex.androidschool.domain.repository.PostRepository

interface TestErrorPostRepository : PostRepository {
    override suspend fun getPosts(): List<Post> = error("Not mocked")

    override suspend fun likeById(
        id: Long,
        isLiked: Boolean
    ): Post = error("Not mocked")

    override suspend fun savePost(post: Post): Post = error("Not mocked")

    override suspend fun deleteById(id: Long) = error("Not mocked")
}