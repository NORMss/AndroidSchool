package com.eltex.androidschool.domain.repository

import com.eltex.androidschool.domain.model.Post
import com.eltex.androidschool.view.model.FileModel

interface PostRepository {
    suspend fun getPosts(): List<Post>
    suspend fun getPostsNewer(id: Long): List<Post>
    suspend fun getPostsBefore(id: Long, count: Int): List<Post>
    suspend fun getPostsAfter(id: Long, count: Int): List<Post>
    suspend fun getPostsLatest(count: Int): List<Post>
    suspend fun likeById(id: Long, isLiked: Boolean): Post
    suspend fun savePost(id: Long, content: String, fileModel: FileModel?): Post
    suspend fun deleteById(id: Long)
}