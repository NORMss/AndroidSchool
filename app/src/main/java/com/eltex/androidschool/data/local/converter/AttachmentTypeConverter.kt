package com.eltex.androidschool.data.local.converter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.eltex.androidschool.domain.model.Attachment
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


@ProvidedTypeConverter
class AttachmentTypeConverter {
    @TypeConverter
    fun attachmentToString(attachment: Attachment): String {
        return Json.encodeToString(attachment)
    }

    @TypeConverter
    fun stringToAttachment(attachment: String): Attachment {
        return Json.decodeFromString<Attachment>(attachment)
    }
}