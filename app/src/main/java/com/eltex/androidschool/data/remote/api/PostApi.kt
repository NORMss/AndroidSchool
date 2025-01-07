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
    suspend fun getPosts(): List<Post>

    @POST("api/posts")
    suspend fun save(@Body post: Post): Post

    @DELETE("api/posts/{id}")
    suspend fun deleteById(@Path("id") id: Long)

    @POST("api/posts/{id}/likes")
    suspend fun likeById(@Path("id") id: Long): Post

    @DELETE("api/posts/{id}/likes")
    suspend fun unlikeById(@Path("id") id: Long): Post

    companion object {
        val INSTANCE: PostApi by lazy {
            RetrofitFactory.INSTANCE.create()
        }
    }
}