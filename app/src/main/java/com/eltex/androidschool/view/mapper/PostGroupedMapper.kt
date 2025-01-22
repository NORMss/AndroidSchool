package com.eltex.androidschool.view.mapper

import com.eltex.androidschool.domain.mapper.Mapper
import com.eltex.androidschool.view.fragment.post.adapter.paging.PostPagingModel
import com.eltex.androidschool.view.model.PostUi
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime

class PostGroupedMapper : Mapper<List<PostUi>, List<PostPagingModel>> {
    override fun map(from: List<PostUi>): List<PostPagingModel> {
        return groupByDateToListItems(from)
    }

    private fun groupByDateToListItems(
        items: List<PostUi>
    ): List<PostPagingModel> {
        val groupedItems = items.groupBy { item ->
            item.published.toLocalDateTime(TimeZone.currentSystemDefault()).date
        }

        return groupedItems.flatMap { (date, groupedItems) ->
            val instant = date.atStartOfDayIn(TimeZone.currentSystemDefault())
            val header = PostPagingModel.Header(instant)
            val itemList = groupedItems.sortedByDescending { it.published }
                .map { PostPagingModel.ItemPost(it) }
            listOf(header) + itemList
        }
    }
}