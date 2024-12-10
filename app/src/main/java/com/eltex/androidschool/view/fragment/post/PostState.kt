package com.eltex.androidschool.view.fragment.post

import com.eltex.androidschool.domain.model.Post
import com.eltex.androidschool.utils.datetime.DateSeparators.GroupByDate
import com.eltex.androidschool.view.common.Status

data class PostState(
    val posts: List<Post> = emptyList(),
    val postsByDate: List<GroupByDate<Post>> = emptyList(),
    val toast: Pair<Int, Boolean>? = null,
    val status: Status = Status.Idle,
) {
    val isRefreshing: Boolean
        get() = status == Status.Loading && posts.isEmpty()
    val isEmptyLoading: Boolean
        get() = status == Status.Loading && posts.isEmpty()
    val isEmptyError: Boolean
        get() = status is Status.Error && posts.isNotEmpty() == true
    val isRefreshError: Boolean
        get() = status is Status.Error && posts.isEmpty()
}