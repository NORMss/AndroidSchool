package com.eltex.androidschool.domain.repository

import com.eltex.androidschool.domain.model.Post

interface PostRepository {
    suspend fun getPosts(): List<Post>
    suspend fun getPostsNewer(id: Long): List<Post>
    suspend fun getPostsBefore(id: Long, count: Int): List<Post>
    suspend fun getPostsAfter(id: Long, count: Int): List<Post>
    suspend fun getPostsLatest(count: Int): List<Post>
    suspend fun likeById(id: Long, isLiked: Boolean): Post
    suspend fun savePost(post: Post): Post
    suspend fun deleteById(id: Long)
}