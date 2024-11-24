package com.eltex.androidschool.utils.datetime

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.toLocalDateTime

object DateTimeStringFormater {
    @OptIn(FormatStringsInDatetimeFormats::class)
    fun dateTimeStringToString(dateTime: Instant): String {
        return try {
            dateTime
                .toLocalDateTime(TimeZone.currentSystemDefault()).let {
                    it.format(LocalDateTime.Format { byUnicodePattern("dd.MM.yyyy HH:mm") })
                }
        } catch (e: Exception) {
            "Unknown"
        }
    }
}