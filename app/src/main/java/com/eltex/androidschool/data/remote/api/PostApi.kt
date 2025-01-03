package com.eltex.androidschool.data.remote.api

import com.eltex.androidschool.data.remote.RetrofitFactory
import com.eltex.androidschool.domain.model.Post
import io.reactivex.rxjava3.core.Single
import retrofit2.create
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PostApi {
    @GET("api/posts")
    fun getPosts(): Single<List<Post>>

    @POST("api/posts")
    fun save(@Body post: Post): Single<Post>

    @DELETE("api/posts/{id}")
    fun deleteById(@Path("id") id: Long): Single<Unit>

    @POST("api/posts/{id}/likes")
    fun likeById(@Path("id") id: Long): Single<Post>

    @DELETE("api/posts/{id}/likes")
    fun unlikeById(@Path("id") id: Long): Single<Post>

    companion object {
        val INSTANCE: PostApi by lazy {
            RetrofitFactory.INSTANCE.create()
        }
    }
}