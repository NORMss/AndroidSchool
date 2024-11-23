package com.eltex.androidschool.view.fragment.post

import com.eltex.androidschool.domain.model.Post
import com.eltex.androidschool.utils.datatime.DateSeparators.GroupByDate

data class PostState(
    val posts: List<Post> = emptyList(),
    val postsByDate: List<GroupByDate<Post>> = emptyList(),
    val toast: Pair<Int, Boolean>? = null,
)