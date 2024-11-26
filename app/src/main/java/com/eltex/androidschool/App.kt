package com.eltex.androidschool

import android.app.Application
import com.eltex.androidschool.data.local.AppDb
import com.eltex.androidschool.data.local.event.EventDaoImpl
import com.eltex.androidschool.data.local.post.PostDaoImpl
import com.eltex.androidschool.data.manager.LocalEventManagerImpl
import com.eltex.androidschool.data.manager.LocalPostsManagerImpl

class App : Application() {
    lateinit var localPostsManager: LocalPostsManagerImpl
        private set

    lateinit var localEventsManager: LocalEventManagerImpl
        private set

    lateinit var postDao: PostDaoImpl
        private set

    lateinit var eventDao: EventDaoImpl
        private set

    private lateinit var appDb: AppDb

    override fun onCreate() {
        super.onCreate()

        appDb = AppDb.getInstance(this)
        postDao = appDb.postDao
        eventDao = appDb.eventDao
    }
}