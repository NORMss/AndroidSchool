package com.eltex.androidschool.view.util.datetime

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.toLocalDateTime

class DateTimeStringFormatter(private val timeZone: TimeZone) {
    @OptIn(FormatStringsInDatetimeFormats::class)
    fun format(dateTime: Instant): String {
        return try {
            dateTime
                .toLocalDateTime(timeZone)
                .format(LocalDateTime.Format { byUnicodePattern("dd.MM.yy HH:mm") })
        } catch (e: Exception) {
            "Unknown"
        }
    }

    companion object {
        fun default() = DateTimeStringFormatter(TimeZone.currentSystemDefault())
    }
}