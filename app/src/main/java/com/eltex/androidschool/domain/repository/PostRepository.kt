package com.eltex.androidschool.domain.repository

import com.eltex.androidschool.domain.model.Post
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface PostRepository {
    fun getPosts(): Single<List<Post>> = Single.never()
    fun likeById(id: Long, isLiked: Boolean): Single<Post> = Single.never()
    fun savePost(post: Post): Single<Post> = Single.never()
    fun deleteById(id: Long): Completable = Completable.complete()
}