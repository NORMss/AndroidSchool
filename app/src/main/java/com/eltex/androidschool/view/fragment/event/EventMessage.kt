package com.eltex.androidschool.view.fragment.event

import com.eltex.androidschool.view.model.PostUi

sealed interface EventMessage {
    data object LoadNextPage : EventMessage
    data object Refresh : EventMessage
    data class Like(val post: PostUi) : EventMessage
    data class Delete(val post: PostUi) : EventMessage
    data class Participate(val post: PostUi) : EventMessage
    data object HandleError : EventMessage
}