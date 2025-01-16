package com.eltex.androidschool.view.mapper

import com.eltex.androidschool.domain.mapper.GroupByDateMapper
import com.eltex.androidschool.view.model.PostUi
import com.eltex.androidschool.view.util.datetime.DateSeparators
import com.eltex.androidschool.view.util.datetime.DateSeparators.GroupByDate

class PostGroupByDateMapper : GroupByDateMapper<PostUi, PostUi> {
    override fun map(from: List<PostUi>): List<GroupByDate<PostUi>> =
        DateSeparators.groupByDate(from).map {
            GroupByDate(
                date = it.date,
                items = it.items,
            )
        }
}