package com.eltex.androidschool.domain.local

import com.eltex.androidschool.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface LocalPostsManager {
    suspend fun addPost(post: Post)
    fun getPosts(): Flow<List<Post>>
    suspend fun deletePost(id: Long)
    suspend fun generateNextId(): Long
    suspend fun updatePost(id: Long, update: (Post) -> Post)
}