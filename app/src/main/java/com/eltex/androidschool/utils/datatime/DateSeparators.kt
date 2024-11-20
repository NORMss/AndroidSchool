package com.eltex.androidschool.utils.datatime

import com.eltex.androidschool.R
import com.eltex.androidschool.utils.resourcemanager.ResourceManager
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
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


    fun <T : Publishable> groupByDate(
        items: List<T>,
        resourceManager: ResourceManager
    ): List<GroupByDate<T>> {
        val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

        val groupedItems = items.groupBy { item ->
            val itemDate =
                Instant.parse(item.published).toLocalDateTime(TimeZone.currentSystemDefault()).date
            when (itemDate) {
                currentDate -> resourceManager.getString(R.string.today)
                currentDate.minus(
                    1,
                    DateTimeUnit.DAY
                ) -> resourceManager.getString(R.string.yesterday)

                else -> dateFormater(itemDate, resourceManager)
            }
        }

        return groupedItems.map { (date, items) ->
            GroupByDate(date, items.reversed())
        }
    }

    private fun dateFormater(
        itemDate: LocalDate,
        resourceManager: ResourceManager
    ): String {
        return itemDate.let {
            "${it.dayOfMonth} ${
                resourceManager.getString(
                    when (it.monthNumber) {
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
            } ${it.year}"
        }
    }
}