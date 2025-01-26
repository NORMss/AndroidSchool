package com.eltex.androidschool.view.fragment.post.adapter.paging

import com.eltex.androidschool.view.model.PostUi
import kotlinx.datetime.Instant

sealed class PostPagingModel {
    data class Header(val date: Instant) : PostPagingModel()
    data class Item(val data: PostUi) : PostPagingModel()
    data object Loading : PostPagingModel()
    data class Error(val reason: Throwable) : PostPagingModel()
}