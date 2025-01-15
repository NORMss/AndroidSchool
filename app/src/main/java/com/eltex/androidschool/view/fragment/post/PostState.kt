package com.eltex.androidschool.view.fragment.post

import com.eltex.androidschool.view.model.PostUi
import com.eltex.androidschool.view.util.datetime.DateSeparators.GroupByDate

data class PostState(
    val posts: List<PostUi> = emptyList(),
    val postsByDate: List<GroupByDate<PostUi>> = emptyList(),
    val status: PostStatus = PostStatus.Idle,
    val singleError: Throwable? = null,
) {
    val isRefreshing: Boolean
        get() = status is PostStatus.EmptyError
    val isEmptyLoading: Boolean
        get() = status is PostStatus.EmptyLoading
    val isEmptyError: Boolean
        get() = status is PostStatus.EmptyError
    val emptyError: Throwable? = (status as? PostStatus.EmptyError)?.reason
}