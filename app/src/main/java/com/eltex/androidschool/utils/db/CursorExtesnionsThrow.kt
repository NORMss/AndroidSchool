package com.eltex.androidschool.utils.db

import android.database.Cursor

object CursorExtensionsThrow {
    fun Cursor.getLongOrThrow(columnName: String): Long =
        getLong(getColumnIndexOrThrow(columnName))

    fun Cursor.getStringOrThrow(columnName: String): String? =
        getString(getColumnIndexOrThrow(columnName)) ?: null

    fun Cursor.getIntOrThrow(columnName: String): Int =
        getInt(getColumnIndexOrThrow(columnName))

    fun Cursor.getBooleanOrThrow(columnName: String): Boolean =
        getInt(getColumnIndexOrThrow(columnName)) != 0
}