package com.eltex.androidschool.domain.repository

import com.eltex.androidschool.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    fun getPost(): Flow<List<Post>>
    fun likeById(id: Long)
    fun deletePostById(id: Long)
    fun editPostById(id: Long, textContent: String)
    fun addPost(textContent: String)
}