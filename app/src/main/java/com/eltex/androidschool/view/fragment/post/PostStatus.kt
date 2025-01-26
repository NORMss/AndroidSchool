package com.eltex.androidschool.view.fragment.post

sealed interface PostStatus {
    data class Idle(val isLoadingFinished: Boolean = false) : PostStatus
    data object Refreshing : PostStatus
    data object NextPageLoading : PostStatus
    data class NextPageError(val reason: Throwable) : PostStatus
    data object EmptyLoading : PostStatus
    data class EmptyError(val reason: Throwable ) : PostStatus
}