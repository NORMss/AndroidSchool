package com.eltex.androidschool.view.fragment.event

sealed interface EventStatus {
    data object Idle : EventStatus
    data object Refreshing : EventStatus
    data object NextPageLoading : EventStatus
    data class NextPageError(val reason: Throwable) : EventStatus
    data object EmptyLoading : EventStatus
    data class EmptyError(val reason: Throwable ) : EventStatus
}