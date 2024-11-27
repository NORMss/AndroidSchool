package com.eltex.androidschool

import android.app.Application
import com.eltex.androidschool.data.local.AppDb
import com.eltex.androidschool.data.local.event.EventDao
import com.eltex.androidschool.data.local.post.PostDao

class App : Application() {
    lateinit var postDao: PostDao
        private set

    lateinit var eventDao: EventDao
        private set

    private lateinit var appDb: AppDb

    override fun onCreate() {
        super.onCreate()

        appDb = AppDb.getInstance(this)

        postDao = appDb.postDao
        eventDao = appDb.eventDao
    }
}