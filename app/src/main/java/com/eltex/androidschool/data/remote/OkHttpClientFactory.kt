package com.eltex.androidschool.data.remote

import com.eltex.androidschool.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

object OkHttpClientFactory {
    val INSTANCE by lazy {
        OkHttpClient.Builder().apply {
            if (BuildConfig.DEBUG) {
                addInterceptor(interceptorLogging)
            } else {
                this
            }
                .addInterceptor {
                    it.proceed(
                        it.request().newBuilder()
                            .addHeader("Api-Key", BuildConfig.API_KEY)
                            .addHeader("Authorization", BuildConfig.USER_TOKEN)
                            .build()
                    )
                }
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build()
        }.build()
    }

    private val interceptorLogging = HttpLoggingInterceptor().apply {
        this.level = HttpLoggingInterceptor.Level.BODY
    }
}
