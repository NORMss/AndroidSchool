package com.eltex.androidschool.view.fragment.post

import com.eltex.androidschool.domain.model.Post
import com.eltex.androidschool.view.common.Status
import com.eltex.androidschool.view.model.PostUi
import com.eltex.androidschool.view.util.datetime.DateSeparators.GroupByDate

data class PostState(
    val posts: List<Post> = emptyList(),
    val postsByDate: List<GroupByDate<PostUi>> = emptyList(),
    val toast: Pair<Int, Boolean>? = null,
    val status: Status = Status.Idle,
) {
    val isRefreshing: Boolean
        get() = status == Status.Loading && posts.isNotEmpty()
    val isEmptyLoading: Boolean
        get() = status == Status.Loading && posts.isEmpty()
    val isEmptyError: Boolean
        get() = status is Status.Error && posts.isEmpty()
    val isRefreshError: Boolean
        get() = status is Status.Error && posts.isNotEmpty()
}