package com.eltex.androidschool

import android.app.Application
import com.eltex.androidschool.data.remote.OkHttpClientFactory
import okhttp3.OkHttpClient

class App : Application() {
    lateinit var client: OkHttpClient
        private set

    override fun onCreate() {
        super.onCreate()

        client = OkHttpClientFactory.INSTANCE
    }
}