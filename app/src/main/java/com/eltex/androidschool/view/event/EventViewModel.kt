package com.eltex.androidschool.view.event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eltex.androidschool.domain.repository.EventRepository
import com.eltex.androidschool.utils.datatime.DateSeparators
import com.eltex.androidschool.utils.resourcemanager.ResourceManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EventViewModel(
    private val eventRepository: EventRepository,
    private val resourceManager: ResourceManager,
) : ViewModel() {
    private val _state = MutableStateFlow(EventState())
    val state = _state.asStateFlow()

    init {
        eventRepository.getEvents().onEach { events ->
            _state.update { state ->
                state.copy(events = events)
            }
            createEventsByDate()
        }.launchIn(viewModelScope)
    }

    fun likeById(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            eventRepository.likeById(id)
            refreshEvents()
        }
    }

    fun participateById(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            eventRepository.participateById(id)
            refreshEvents()
        }
    }

    private suspend fun refreshEvents() {
        val events = eventRepository.getEvents().first()
        _state.update { state ->
            state.copy(events = events)
        }
        createEventsByDate()
    }

    private fun createEventsByDate() {
        if (_state.value.events.isNotEmpty()) {
            _state.update {
                it.copy(
                    eventsByDate = DateSeparators.groupByDate(
                        items = _state.value.events,
                        resourceManager = resourceManager,
                    )
                )
            }
        }
    }

    fun deleteEvent(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            eventRepository.deleteEventById(id)
            refreshEvents()
        }
    }

    fun editEvent(id: Long, textContent: String) {
        viewModelScope.launch(Dispatchers.IO) {
            eventRepository.editEventById(id, textContent)
            refreshEvents()
        }
    }

    fun addEvent(textContent: String, imageContent: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            eventRepository.addEvent(textContent, imageContent)
            refreshEvents()
        }
    }
}