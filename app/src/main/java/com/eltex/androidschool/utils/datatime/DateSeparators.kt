package com.eltex.androidschool.utils.datatime

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime

object DateSeparators {
    interface Publishable {
        val published: String
    }

    data class GroupByDate<T>(
        val date: String,
        val items: List<T>
    )


    fun <T : Publishable> groupByDate(items: List<T>): List<GroupByDate<T>> {
        val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

        val groupedItems = items.groupBy { item ->
            val itemDate =
                Instant.parse(item.published).toLocalDateTime(TimeZone.currentSystemDefault()).date
            when (itemDate) {
                currentDate -> "Сегодня"
                currentDate.minus(1, DateTimeUnit.DAY) -> "Вчера"
                else -> itemDate.toString()
            }
        }

        return groupedItems.map { (date, items) ->
            GroupByDate(date, items)
        }.reversed()
    }
}