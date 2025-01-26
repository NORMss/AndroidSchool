package com.eltex.androidschool.view.mapper

import com.eltex.androidschool.domain.mapper.Mapper
import com.eltex.androidschool.view.fragment.post.PostState
import com.eltex.androidschool.view.fragment.post.PostStatus
import com.eltex.androidschool.view.fragment.post.adapter.paging.PagingModel
import com.eltex.androidschool.view.fragment.post.reducer.PostReducer.Companion.PAGE_SIZE
import com.eltex.androidschool.view.model.PostUi
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime

class PostPagingMapper : Mapper<PostState, List<PagingModel>> {
    override fun map(from: PostState): List<PagingModel> {
        val groupedPosts = groupPostsByDate(from.posts)

        return when (val statusValue = from.status) {
            PostStatus.NextPageLoading -> groupedPosts + List(PAGE_SIZE) { PagingModel.Loading }
            is PostStatus.NextPageError -> groupedPosts + PagingModel.Error(statusValue.reason)
            is PostStatus.EmptyError,
            PostStatus.Idle -> groupedPosts

            PostStatus.EmptyLoading -> List(PAGE_SIZE) { PagingModel.Loading }

            PostStatus.Refreshing -> groupedPosts
        }
    }

    private fun groupPostsByDate(
        items: List<PostUi>
    ): List<PagingModel> {
        val groupedItems = items.groupBy { item ->
            item.published.toLocalDateTime(TimeZone.currentSystemDefault()).date
        }

        return groupedItems.flatMap { (date, groupedItems) ->
            val instant = date.atStartOfDayIn(TimeZone.currentSystemDefault())
            val header = PagingModel.Header(instant)
            val itemList = groupedItems.sortedByDescending { it.published }
                .map { PagingModel.Item(it) }
            listOf(header) + itemList
        }
    }
}