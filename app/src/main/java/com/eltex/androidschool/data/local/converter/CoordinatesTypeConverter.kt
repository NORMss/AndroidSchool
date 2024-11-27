package com.eltex.androidschool.data.local.converter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.eltex.androidschool.domain.model.Coordinates
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


@ProvidedTypeConverter
class CoordinatesTypeConverter {
    @TypeConverter
    fun coordinatesToString(coordinates: Coordinates): String {
        return Json.encodeToString(coordinates)
    }

    @TypeConverter
    fun stringToCoordinates(coordinates: String): Coordinates {
        return Json.decodeFromString<Coordinates>(coordinates)
    }
}