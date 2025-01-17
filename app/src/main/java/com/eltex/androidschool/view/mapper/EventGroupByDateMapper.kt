package com.eltex.androidschool.view.mapper

import com.eltex.androidschool.domain.mapper.GroupByDateMapper
import com.eltex.androidschool.view.model.EventUi
import com.eltex.androidschool.view.util.datetime.DateSeparators
import com.eltex.androidschool.view.util.datetime.DateSeparators.GroupByDate

class EventGroupByDateMapper : GroupByDateMapper<EventUi, EventUi> {
    override fun map(from: List<EventUi>): List<GroupByDate<EventUi>> =
        DateSeparators.groupByDate(from).map {
            GroupByDate(
                date = it.date,
                items = it.items,
            )
        }
}