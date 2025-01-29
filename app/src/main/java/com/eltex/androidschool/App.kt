package com.eltex.androidschool

import android.app.Application
import com.eltex.androidschool.data.remote.OkHttpClientFactory
import com.eltex.androidschool.data.remote.api.EventApi
import com.eltex.androidschool.data.remote.api.MediaApi
import com.eltex.androidschool.data.remote.api.PostApi
import com.eltex.androidschool.data.remote.api.UserApi
import okhttp3.OkHttpClient

class App : Application() {
    lateinit var postApi: PostApi
        private set

    lateinit var eventApi: EventApi
        private set

    lateinit var userApi: UserApi
        private set

    lateinit var mediaApi: MediaApi
        private set

    lateinit var client: OkHttpClient
        private set

    override fun onCreate() {
        super.onCreate()

        client = OkHttpClientFactory.INSTANCE

        postApi = PostApi.INSTANCE

        eventApi = EventApi.INSTANCE

        userApi = UserApi.INSTANCE

        mediaApi = MediaApi.INSTANCE
    }
}