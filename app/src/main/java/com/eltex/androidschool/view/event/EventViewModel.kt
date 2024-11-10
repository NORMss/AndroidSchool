package com.eltex.androidschool.view.event

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eltex.androidschool.R
import com.eltex.androidschool.domain.repository.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class EventViewModel(
    private val eventRepository: EventRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(EventState())
    val state = _state.asStateFlow()

    init {
        eventRepository.getEvent().onEach { event ->
            _state.update { state ->
                state.copy(event = event)
            }
        }.launchIn(viewModelScope)
    }

    fun like() {
        eventRepository.like()
    }

    fun share() {
        sendToast(R.string.not_implemented, true)
    }

    fun participate() {
        eventRepository.participate()
    }

    private fun sendToast(@StringRes res: Int, short: Boolean = true) {
        _state.update {
            it.copy(
                toast = Pair(res, short)
            )
        }
    }
}