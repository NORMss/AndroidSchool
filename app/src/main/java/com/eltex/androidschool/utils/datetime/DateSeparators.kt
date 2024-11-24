package com.eltex.androidschool.utils.datetime

import com.eltex.androidschool.R
import com.eltex.androidschool.utils.resourcemanager.ResourceManager
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime

object DateSeparators {

    interface Publishable {
        val published: Instant
    }

    data class GroupByDate<T>(
        val date: Instant,
        val items: List<T>
    )

    fun <T : Publishable> groupByDate(
        items: List<T>
    ): List<GroupByDate<T>> {
        val groupedItems = items.groupBy { item ->
            item.published.toLocalDateTime(TimeZone.currentSystemDefault()).date
        }

        return groupedItems.map { (date, groupedItems) ->
            val instant = date.atStartOfDayIn(TimeZone.currentSystemDefault())
            GroupByDate(instant, groupedItems.sortedByDescending { it.published })
        }.reversed()
    }

    fun formatInstantToString(
        instant: Instant,
        resourceManager: ResourceManager
    ): String {
        val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val localDate = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date

        return when (localDate) {
            currentDate -> resourceManager.getString(R.string.today)
            currentDate.minus(1, DateTimeUnit.DAY) -> resourceManager.getString(R.string.yesterday)
            else -> formatLocalDate(localDate, resourceManager)
        }
    }

    private fun formatLocalDate(
        localDate: LocalDate,
        resourceManager: ResourceManager
    ): String {
        val monthName = resourceManager.getString(
            when (localDate.monthNumber) {
                1 -> R.string.january
                2 -> R.string.february
                3 -> R.string.march
                4 -> R.string.april
                5 -> R.string.may
                6 -> R.string.june
                7 -> R.string.july
                8 -> R.string.august
                9 -> R.string.september
                10 -> R.string.october
                11 -> R.string.november
                12 -> R.string.december
                else -> R.string.blank
            }
        )
        return "${localDate.dayOfMonth} $monthName ${localDate.year}"
    }
}
