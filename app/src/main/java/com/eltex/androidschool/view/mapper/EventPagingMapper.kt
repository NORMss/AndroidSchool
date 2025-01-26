package com.eltex.androidschool.view.mapper

import com.eltex.androidschool.domain.mapper.Mapper
import com.eltex.androidschool.view.fragment.event.EventState
import com.eltex.androidschool.view.fragment.event.EventStatus
import com.eltex.androidschool.view.fragment.event.adapter.paging.EventPagingModel
import com.eltex.androidschool.view.fragment.event.reducer.EventReducer.Companion.PAGE_SIZE
import com.eltex.androidschool.view.model.EventUi
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import kotlin.collections.component1
import kotlin.collections.component2

class EventPagingMapper : Mapper<EventState, List<EventPagingModel>> {
    override fun map(from: EventState): List<EventPagingModel> {
        val groupedEvent = groupEventsByDate(from.events)

        return when (val statusValue = from.status) {
            EventStatus.NextPageLoading -> groupedEvent + List(PAGE_SIZE) { EventPagingModel.Loading }
            is EventStatus.NextPageError -> groupedEvent + EventPagingModel.Error(statusValue.reason)
            is EventStatus.EmptyError,
            EventStatus.Idle -> groupedEvent

            EventStatus.EmptyLoading -> List(PAGE_SIZE) { EventPagingModel.Loading }

            EventStatus.Refreshing -> groupedEvent
        }
    }

    private fun groupEventsByDate(
        items: List<EventUi>
    ): List<EventPagingModel> {
        val groupedItems = items.groupBy { item ->
            item.published.toLocalDateTime(TimeZone.currentSystemDefault()).date
        }

        return groupedItems.flatMap { (date, groupedItems) ->
            val instant = date.atStartOfDayIn(TimeZone.currentSystemDefault())
            val header = EventPagingModel.Header(instant)
            val itemList = groupedItems.sortedByDescending { it.published }
                .map { EventPagingModel.Item(it) }
            listOf(header) + itemList
        }
    }
}