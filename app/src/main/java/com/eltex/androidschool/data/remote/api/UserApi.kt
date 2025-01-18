package com.eltex.androidschool.data.remote.api

import com.eltex.androidschool.data.remote.RetrofitFactory
import com.eltex.androidschool.domain.model.User
import com.eltex.androidschool.domain.model.UserAuthentication
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApi {
    @POST("api/users/registration")
    suspend fun registrationUser(
        @Query("login") login: String,
        @Query("pass") pass: String,
        @Query("name") name: String,
    )

    @POST("api/users/authentication")
    suspend fun authenticationUser(
        @Query("login") login: String,
        @Query("pass") pass: String,
    ): UserAuthentication

    @GET("api/users")
    suspend fun getUsers(): List<User>

    @GET("api/users/{id}")
    suspend fun getUser(@Path("id") id: Long): User

    companion object {
        val INSTANCE: UserApi by lazy {
            RetrofitFactory.INSTANCE.create()
        }
    }

}