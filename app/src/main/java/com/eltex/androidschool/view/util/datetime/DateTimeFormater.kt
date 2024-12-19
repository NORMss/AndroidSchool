package com.eltex.androidschool.view.util.datetime

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.toLocalDateTime

object DateTimeStringFormater {
    @OptIn(FormatStringsInDatetimeFormats::class)
    fun dateTimeToString(dateTime: Instant): String {
        return try {
            dateTime
                .toLocalDateTime(TimeZone.currentSystemDefault()).let {
                    it.format(LocalDateTime.Format { byUnicodePattern("dd.MM.yy HH:mm") })
                }
        } catch (e: Exception) {
            "Unknown"
        }
    }
}