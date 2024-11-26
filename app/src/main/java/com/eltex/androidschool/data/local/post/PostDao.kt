package com.eltex.androidschool.data.local.post

import com.eltex.androidschool.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface PostDao {
    fun getPosts(): Flow<List<Post>>
    suspend fun getPost(id: Long): Post?
    suspend fun addPost(post: Post)
    suspend fun deletePost(id: Long)
    suspend fun updatePost(id: Long, post: Post)
}