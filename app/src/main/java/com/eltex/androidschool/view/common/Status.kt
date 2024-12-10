package com.eltex.androidschool.view.common

sealed interface Status {
    val throwableOtNull: Throwable?
        get() = (this as? Error)?.throwable

    data object Idle : Status
    data object Loading : Status
    data class Error(val throwable: Throwable) : Status
}