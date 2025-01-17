package com.eltex.androidschool.view.fragment.event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EventViewModel(
    private val store: EventStore,
) : ViewModel() {
    val state: StateFlow<EventState> = store.state

    init {
        viewModelScope.launch {
            store.connect()
        }
    }

    fun accept(message: EventMessage) {
        store.accept(message)
    }
}
