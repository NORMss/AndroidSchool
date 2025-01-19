package com.eltex.androidschool.view.fragment.post.adapter.grouped

import com.eltex.androidschool.view.model.PostUi
import kotlinx.datetime.Instant

sealed class ListItem {
    data class Header(val date: Instant) : ListItem()
    data class Item(val postUI: PostUi) : ListItem()
}