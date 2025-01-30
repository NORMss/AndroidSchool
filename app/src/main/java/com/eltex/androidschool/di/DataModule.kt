package com.eltex.androidschool.di

import com.eltex.androidschool.BuildConfig
import com.eltex.androidschool.data.remote.api.EventApi
import com.eltex.androidschool.data.remote.api.MediaApi
import com.eltex.androidschool.data.remote.api.PostApi
import com.eltex.androidschool.utils.constants.Remote.BASE_URL_ELTEX_ANDROID
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit


@InstallIn(SingletonComponent::class)
@Module
object DataModule {
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().apply {
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

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient) = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(BASE_URL_ELTEX_ANDROID)
        .addConverterFactory(json.asConverterFactory(contentType))
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()

    @Singleton
    @Provides
    fun provideMediaApi(retrofit: Retrofit): MediaApi = retrofit.create()
    @Singleton
    @Provides
    fun providePostApi(retrofit: Retrofit): PostApi = retrofit.create()
    @Singleton
    @Provides
    fun provideEventApi(retrofit: Retrofit): EventApi = retrofit.create()

    private val contentType = "application/json".toMediaType()
    private val json = Json { ignoreUnknownKeys = true }

    private val interceptorLogging = HttpLoggingInterceptor().apply {
        this.level = HttpLoggingInterceptor.Level.BODY
    }
}