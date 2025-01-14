package com.eltex.androidschool.view.model

data class EventWithError(
    val event: EventUi,
    val error: Throwable,
)