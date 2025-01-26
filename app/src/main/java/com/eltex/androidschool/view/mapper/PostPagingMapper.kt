package com.eltex.androidschool.view.mapper

import com.eltex.androidschool.domain.mapper.Mapper
import com.eltex.androidschool.view.fragment.post.PostState
import com.eltex.androidschool.view.fragment.post.PostStatus
import com.eltex.androidschool.view.fragment.post.adapter.paging.PostPagingModel
import com.eltex.androidschool.view.fragment.post.reducer.PostReducer.Companion.PAGE_SIZE
import com.eltex.androidschool.view.model.PostUi
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime

class PostPagingMapper : Mapper<PostState, List<PostPagingModel>> {
    override fun map(from: PostState): List<PostPagingModel> {
        val groupedPosts = groupPostsByDate(from.posts)

        return when (val statusValue = from.status) {
            PostStatus.NextPageLoading -> groupedPosts + List(PAGE_SIZE) { PostPagingModel.Loading }
            is PostStatus.NextPageError -> groupedPosts + PostPagingModel.Error(statusValue.reason)
            is PostStatus.EmptyError,
            PostStatus.Idle -> groupedPosts

            PostStatus.EmptyLoading -> List(PAGE_SIZE) { PostPagingModel.Loading }

            PostStatus.Refreshing -> groupedPosts
        }
    }

    private fun groupPostsByDate(
        items: List<PostUi>
    ): List<PostPagingModel> {
        val groupedItems = items.groupBy { item ->
            item.published.toLocalDateTime(TimeZone.currentSystemDefault()).date
        }

        return groupedItems.flatMap { (date, groupedItems) ->
            val instant = date.atStartOfDayIn(TimeZone.currentSystemDefault())
            val header = PostPagingModel.Header(instant)
            val itemList = groupedItems.sortedByDescending { it.published }
                .map { PostPagingModel.Item(it) }
            listOf(header) + itemList
        }
    }
}