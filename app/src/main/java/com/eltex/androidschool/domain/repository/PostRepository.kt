package com.eltex.androidschool.domain.repository

import com.eltex.androidschool.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    fun getPost(): Flow<Post>
    fun like()
}