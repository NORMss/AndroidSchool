package com.eltex.androidschool

import android.app.Application
import com.eltex.androidschool.data.local.LocalEventManagerImpl
import com.eltex.androidschool.data.local.LocalPostsManagerImpl

class App : Application() {
    lateinit var localPostsManager: LocalPostsManagerImpl
        private set

    lateinit var localEventsManager: LocalEventManagerImpl
        private set

    override fun onCreate() {
        super.onCreate()
        localPostsManager = LocalPostsManagerImpl.getInstance(this)
        localEventsManager = LocalEventManagerImpl.getInstance(this)
    }
}