package com.eltex.androidschool.view.fragment.event

import com.eltex.androidschool.view.model.PostUi

sealed interface EventEffect {
    data class LoadNextPage(val id: Long, val count: Int) : EventEffect
    data class LoadInitialPage(val count: Int) : EventEffect
    data class Like(val post: PostUi) : EventEffect
    data class Delete(val post: PostUi) : EventEffect
    data class Participate(val post: PostUi) : EventEffect
}