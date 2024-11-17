package com.eltex.androidschool.utils.datatime

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.toLocalDateTime

object DateTimeStringFormater {
    @OptIn(FormatStringsInDatetimeFormats::class)
    fun dateTimeStringToString(dateTime: String): String {
        return try {
            Instant.parse(dateTime)
                .toLocalDateTime(TimeZone.currentSystemDefault()).let {
                    it.format(LocalDateTime.Format { byUnicodePattern("dd.MM.yyyy HH:mm") })
                }
        } catch (e: Exception) {
            "Unknown"
        }
    }
}