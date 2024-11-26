package com.eltex.androidschool.data.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.eltex.androidschool.data.local.event.EventDaoImpl
import com.eltex.androidschool.data.local.post.PostDaoImpl

class AppDb(db: SQLiteDatabase) {
    val postDao = PostDaoImpl(db)
    val eventDao = EventDaoImpl(db)

    companion object {
        @Volatile
        private var instance: AppDb? = null

        fun getInstance(context: Context): AppDb {
            val application = context.applicationContext


            instance?.let {
                return it
            }

            synchronized(this) {
                instance?.let { return it }
            }

            val dbHelper = AppDb(DbHelper(application).writableDatabase)


            instance = dbHelper

            return dbHelper
        }

    }
}