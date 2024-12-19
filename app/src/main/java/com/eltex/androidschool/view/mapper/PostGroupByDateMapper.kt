package com.eltex.androidschool.view.mapper

import com.eltex.androidschool.domain.mapper.GroupByDateMapper
import com.eltex.androidschool.domain.model.Post
import com.eltex.androidschool.view.model.PostUi
import com.eltex.androidschool.view.util.datetime.DateSeparators
import com.eltex.androidschool.view.util.datetime.DateSeparators.GroupByDate

class PostGroupByDateMapper : GroupByDateMapper<Post, PostUi> {
    override fun map(from: List<Post>): List<GroupByDate<PostUi>> =
        DateSeparators.groupByDate(from).map {
            GroupByDate(
                date = it.date,
                items = it.items.map { PostUiMapper().map(it) },
            )
        }
}