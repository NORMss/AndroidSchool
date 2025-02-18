package com.eltex.androidschool.data.remote.api

import com.eltex.androidschool.data.remote.RetrofitFactory
import com.eltex.androidschool.data.remote.dto.Media
import okhttp3.MultipartBody
import retrofit2.create
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface MediaApi {
    @Multipart
    @POST("api/media")
    suspend fun upload(@Part file: MultipartBody.Part): Media

    companion object {
        val INSTANCE: MediaApi by lazy {
            RetrofitFactory.INSTANCE.create()
        }
    }
}