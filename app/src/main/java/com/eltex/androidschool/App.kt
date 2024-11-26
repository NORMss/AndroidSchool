package com.eltex.androidschool

import android.app.Application
import com.eltex.androidschool.data.local.PostDaoImpl
import com.eltex.androidschool.data.manager.LocalEventManagerImpl
import com.eltex.androidschool.data.manager.LocalPostsManagerImpl

class App : Application() {
    lateinit var localPostsManager: LocalPostsManagerImpl
        private set

    lateinit var localEventsManager: LocalEventManagerImpl
        private set

    lateinit var postDao: PostDaoImpl
        private set

    override fun onCreate() {
        super.onCreate()
        localPostsManager = LocalPostsManagerImpl.getInstance(this)
        localEventsManager = LocalEventManagerImpl.getInstance(this)
        postDao = PostDaoImpl.getInstance(this)
    }
}