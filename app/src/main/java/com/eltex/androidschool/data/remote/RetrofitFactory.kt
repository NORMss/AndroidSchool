package com.eltex.androidschool.data.remote

import com.eltex.androidschool.utils.constants.Remote.BASE_URL_ELTEX_ANDROID
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object RetrofitFactory {
    private val contentType = "application/json".toMediaType()
    private val json = Json { ignoreUnknownKeys = true }
    val INSTANCE: Retrofit by lazy {
        Retrofit.Builder()
            .client(OkHttpClientFactory.INSTANCE)
            .baseUrl(BASE_URL_ELTEX_ANDROID)
            .addConverterFactory(json.asConverterFactory(contentType))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }
}