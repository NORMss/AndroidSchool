package com.eltex.androidschool.view.fragment.event

import com.eltex.androidschool.view.model.EventUi

sealed interface EventEffect {
    data class LoadNextPage(val id: Long, val count: Int) : EventEffect
    data class LoadInitialPage(val count: Int) : EventEffect
    data class Like(val event: EventUi) : EventEffect
    data class Participant(val event: EventUi) : EventEffect
    data class Delete(val event: EventUi) : EventEffect
}