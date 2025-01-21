package com.eltex.androidschool.view.mapper

import com.eltex.androidschool.domain.mapper.Mapper
import com.eltex.androidschool.view.fragment.event.adapter.grouped.EventListItem
import com.eltex.androidschool.view.model.EventUi
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime

class EventGroupedMapper : Mapper<List<EventUi>, List<EventListItem>> {
    override fun map(from: List<EventUi>): List<EventListItem> {
        return groupByDateToListItems(from)
    }

    private fun groupByDateToListItems(
        items: List<EventUi>
    ): List<EventListItem> {
        val groupedItems = items.groupBy { item ->
            item.published.toLocalDateTime(TimeZone.currentSystemDefault()).date
        }

        return groupedItems.flatMap { (date, groupedItems) ->
            val instant = date.atStartOfDayIn(TimeZone.currentSystemDefault())
            val header = EventListItem.Header(instant)
            val itemList = groupedItems.sortedByDescending { it.published }
                .map { EventListItem.ItemEvent(it) }
            listOf(header) + itemList
        }
    }
}