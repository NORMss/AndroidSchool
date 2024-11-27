package com.eltex.androidschool.data.local.converter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import kotlinx.datetime.Instant


@ProvidedTypeConverter
class InstantTypeConverter {
    @TypeConverter
    fun instantToString(instant: Instant): String {
        return instant.toString()
    }

    @TypeConverter
    fun stringToInstant(instant: String): Instant {
        return Instant.parse(instant)
    }
}