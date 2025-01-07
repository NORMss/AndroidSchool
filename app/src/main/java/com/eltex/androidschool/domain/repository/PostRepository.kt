package com.eltex.androidschool.domain.repository

import com.eltex.androidschool.domain.model.Post

interface PostRepository {
    suspend fun getPosts(): List<Post>
    suspend fun likeById(id: Long, isLiked: Boolean): Post
    suspend fun savePost(post: Post): Post
    suspend fun deleteById(id: Long)
}