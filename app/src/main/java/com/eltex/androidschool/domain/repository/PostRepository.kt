package com.eltex.androidschool.domain.repository

import com.eltex.androidschool.domain.model.Post
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface PostRepository {
    fun getPosts(): Single<List<Post>>
    fun likeById(id: Long, isLiked: Boolean): Single<Post>
    fun savePost(post: Post): Single<Post>
    fun deleteById(id: Long): Completable
}