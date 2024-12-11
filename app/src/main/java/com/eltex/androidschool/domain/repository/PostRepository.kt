package com.eltex.androidschool.domain.repository

import com.eltex.androidschool.domain.model.Post
import com.eltex.androidschool.utils.remote.Callback

interface PostRepository {
    fun getPosts(callback: Callback<List<Post>>)
    fun likeById(id: Long, isLiked: Boolean, callback: Callback<Post>)
    fun savePost(post: Post, callback: Callback<Post>)
    fun deleteById(id: Long, callback: Callback<Unit>)
}