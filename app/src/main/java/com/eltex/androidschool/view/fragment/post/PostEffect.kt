package com.eltex.androidschool.view.fragment.post

import com.eltex.androidschool.view.model.PostUi

sealed interface PostEffect {
    data class LoadNextPage(val id: Long, val count: Int) : PostEffect
    data class LoadInitialPage(val count: Int) : PostEffect
    data class Like(val post: PostUi) : PostEffect
    data class Delete(val post: PostUi) : PostEffect
}