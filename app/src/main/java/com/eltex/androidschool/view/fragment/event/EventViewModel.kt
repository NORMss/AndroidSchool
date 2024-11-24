package com.eltex.androidschool.view.fragment.event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eltex.androidschool.domain.model.Event
import com.eltex.androidschool.domain.repository.EventRepository
import com.eltex.androidschool.utils.datatime.DateSeparators
import com.eltex.androidschool.utils.resourcemanager.ResourceManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
        observeEvents()
    }

    fun likeById(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            eventRepository.likeById(id)
        }
    }

    fun participateById(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            eventRepository.participateById(id)
        }
    }

    fun deleteEvent(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            eventRepository.deleteEventById(id)
        }
    }

    fun editEvent(id: Long, textContent: String) {
        viewModelScope.launch(Dispatchers.IO) {
            eventRepository.editEventById(id, textContent)
        }
    }

    private fun createEventsByDate(updatedEvents: List<Event>) {
        _state.update { state ->
            val groupedEvents = DateSeparators.groupByDate(
                items = updatedEvents,
                resourceManager = resourceManager,
            )
            state.copy(events = updatedEvents, eventsByDate = groupedEvents)
        }
    }

    private fun observeEvents() {
        eventRepository.getEvents()
            .onEach { events ->
                _state.update { state ->
                    state.copy(events = events)
                }
                createEventsByDate(events)
            }
            .launchIn(viewModelScope)
    }
}
