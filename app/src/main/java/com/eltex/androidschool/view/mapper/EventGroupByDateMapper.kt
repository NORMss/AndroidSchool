package com.eltex.androidschool.view.mapper

import com.eltex.androidschool.domain.mapper.GroupByDateMapper
import com.eltex.androidschool.domain.model.Event
import com.eltex.androidschool.view.model.EventUi
import com.eltex.androidschool.view.util.datetime.DateSeparators
import com.eltex.androidschool.view.util.datetime.DateSeparators.GroupByDate

class EventGroupByDateMapper : GroupByDateMapper<Event, EventUi> {
    override fun map(from: List<Event>): List<GroupByDate<EventUi>> =
        DateSeparators.groupByDate(from).map {
            GroupByDate(
                date = it.date,
                items = it.items.map { EventUiMapper().map(it) },
            )
        }
}