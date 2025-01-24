package com.eltex.androidschool.view.fragment.post.adapter.paging

import com.eltex.androidschool.view.model.PostUi
import kotlinx.datetime.Instant

sealed class PagingModel {
    data class Header(val date: Instant) : PagingModel()
    data class Item(val data: PostUi) : PagingModel()
    data object Loading : PagingModel()
    data class Error(val reason: Throwable) : PagingModel()
}