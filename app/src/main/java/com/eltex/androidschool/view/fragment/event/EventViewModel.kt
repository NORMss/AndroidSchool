package com.eltex.androidschool.view.fragment.event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eltex.androidschool.domain.mapper.GroupByDateMapper
import com.eltex.androidschool.domain.model.Event
import com.eltex.androidschool.domain.repository.EventRepository
import com.eltex.androidschool.view.common.Status
import com.eltex.androidschool.view.model.EventUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EventViewModel(
    private val eventRepository: EventRepository,
    private val mapper: GroupByDateMapper<Event, EventUi>,
) : ViewModel() {

    val state: StateFlow<EventState>
        field = MutableStateFlow(EventState())

    init {
        loadEvents()
    }

    fun likeById(id: Long, isLiked: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val updatedEvent = eventRepository.likeById(id = id, isLiked = isLiked)
                val updatedEvents = withContext(Dispatchers.Default) {
                    state.value.events.map { event ->
                        if (event.id == updatedEvent.id) updatedEvent else event
                    }
                }
                val eventsUi = withContext(Dispatchers.Default) {
                    mapper.map(updatedEvents)
                }
                state.update {
                    it.copy(
                        events = updatedEvents,
                        eventsByDate = eventsUi,
                        status = Status.Idle,
                    )
                }

            } catch (e: Exception) {
                state.update {
                    it.copy(
                        status = Status.Error(e),
                    )
                }
            }
        }
    }

    fun participateById(id: Long, isParticipated: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val updatedEvent =
                    eventRepository.participateById(id = id, isParticipated = isParticipated)
                val updatedEvents = withContext(Dispatchers.Default) {
                    state.value.events.map { event ->
                        if (event.id == updatedEvent.id) updatedEvent else event
                    }
                }
                val eventsUi = withContext(Dispatchers.Default) {
                    mapper.map(updatedEvents)
                }
                state.update {
                    it.copy(
                        events = updatedEvents,
                        eventsByDate = eventsUi,
                        status = Status.Idle,
                    )
                }
            } catch (e: Exception) {
                state.update {
                    it.copy(
                        status = Status.Error(e),
                    )
                }
            }
        }
    }

    fun deleteEvent(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                eventRepository.deleteById(id)
                val updatedEvents = withContext(Dispatchers.Default) {
                    state.value.events.filter { it.id != id }
                }
                val eventsUi = withContext(Dispatchers.IO) {
                    mapper.map(updatedEvents)
                }
                state.update {
                    it.copy(
                        events = updatedEvents,
                        eventsByDate = eventsUi,
                        status = Status.Idle,
                    )
                }
            } catch (e: Exception) {
                state.update {
                    it.copy(
                        status = Status.Error(e),
                    )
                }
            }
        }
    }

    fun loadEvents() {
        state.update { it.copy(status = Status.Loading) }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val loadedEvents = eventRepository.getEvents()
                val eventsUi = withContext(Dispatchers.Default) {
                    mapper.map(loadedEvents)
                }
                state.update {
                    it.copy(
                        events = loadedEvents,
                        eventsByDate = eventsUi,
                        status = Status.Idle,
                    )
                }
            } catch (e: Exception) {
                state.update {
                    it.copy(
                        status = Status.Error(e),
                    )
                }
            }
        }
    }

    fun consumerError() {
        state.update {
            it.copy(
                status = Status.Idle,
            )
        }
    }
}
