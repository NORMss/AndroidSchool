package com.eltex.androidschool.view.fragment.post.adapter.paging

import com.eltex.androidschool.view.model.PostUi
import kotlinx.datetime.Instant

sealed class PostPagingModel {
    data class Header(val date: Instant) : PostPagingModel()
    data class ItemPost(val data: PostUi) : PostPagingModel()
}