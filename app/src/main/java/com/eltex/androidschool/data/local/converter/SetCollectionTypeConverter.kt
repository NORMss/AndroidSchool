package com.eltex.androidschool.data.local.converter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


@ProvidedTypeConverter
class SetCollectionTypeConverter {
    @TypeConverter
    fun setCollectionToString(setCollection: Set<Long>): String {
        return Json.encodeToString(setCollection)
    }

    @TypeConverter
    fun stringToSetCollection(setCollection: String): Set<Long> {
        return Json.decodeFromString(setCollection)
    }
}