package com.eltex.androidschool.view.fragment.post.adapter.grouped

import com.eltex.androidschool.view.model.PostUi
import kotlinx.datetime.Instant

sealed class PostListItem {
    data class Header(val date: Instant) : PostListItem()
    data class ItemPost(val data: PostUi) : PostListItem()
}