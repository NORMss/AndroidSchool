package com.eltex.androidschool.data.local.converter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.eltex.androidschool.domain.model.UserPreview
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


@ProvidedTypeConverter
class UserPreviewTypeConverter {
    @TypeConverter
    fun userPreviewToString(userPreview: UserPreview): String {
        return Json.encodeToString(userPreview)
    }

    @TypeConverter
    fun stringToUserPreview(userPreview: String): UserPreview {
        return Json.decodeFromString<UserPreview>(userPreview)
    }

    @TypeConverter
    fun listUserPreviewToString(usersPreview: List<UserPreview>): String {
        return Json.encodeToString(usersPreview)
    }

    @TypeConverter
    fun stringToListUserPreview(usersPreview: String): List<UserPreview> {
        return Json.decodeFromString<List<UserPreview>>(usersPreview)
    }
}