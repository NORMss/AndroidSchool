package com.eltex.androidschool.domain.repository

import com.eltex.androidschool.domain.model.Attachment
import com.eltex.androidschool.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    fun getPosts(): Flow<List<Post>>
    suspend fun getPostById(id: Long): Post
    suspend fun likeById(id: Long)
    suspend fun deletePostById(id: Long)
    suspend fun editPostById(id: Long, textContent: String)
    suspend fun addPost(textContent: String, attachment: Attachment?)
}