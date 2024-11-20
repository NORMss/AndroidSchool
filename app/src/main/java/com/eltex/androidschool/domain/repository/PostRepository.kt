package com.eltex.androidschool.domain.repository

import com.eltex.androidschool.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    fun getPost(): Flow<List<Post>>
    fun likeById(id: Long)
    suspend fun deletePostById(id: Long)
    suspend fun editPostById(id: Long, textContent: String)
    suspend fun addPost(textContent: String, contentImage: String?)
}