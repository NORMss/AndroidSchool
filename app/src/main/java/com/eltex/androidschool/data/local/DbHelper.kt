package com.eltex.androidschool.data.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.eltex.androidschool.utils.constants.Db.DB_NAME

class DbHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE ${PostTable.TABLE_NAME} (
                ${PostTable.ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                ${PostTable.AUTHOR_ID} INTEGER NOT NULL,
                ${PostTable.AUTHOR} TEXT NOT NULL,
                ${PostTable.AUTHOR_JOB} TEXT NOT NULL,
                ${PostTable.AUTHOR_AVATAR} TEXT,
                ${PostTable.CONTENT} TEXT NOT NULL,
                ${PostTable.PUBLISHED} TEXT NOT NULL,
                ${PostTable.COORDINATES} TEXT,
                ${PostTable.LINK} TEXT,
                ${PostTable.MENTIONED_ME} INTEGER NOT NULL DEFAULT 0,
                ${PostTable.LIKED_BY_ME} INTEGER NOT NULL DEFAULT 0,
                ${PostTable.ATTACHMENT} TEXT
            )
            """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS ${PostTable.TABLE_NAME}")
        onCreate(db)
    }
}
