package com.eltex.androidschool.view.fragment.event

import androidx.lifecycle.ViewModel
import com.eltex.androidschool.domain.model.Event
import com.eltex.androidschool.domain.repository.EventRepository
import com.eltex.androidschool.utils.datetime.DateSeparators
import com.eltex.androidschool.utils.remote.Callback
import com.eltex.androidschool.view.common.Status
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class EventViewModel(
    private val eventRepository: EventRepository,
) : ViewModel() {
    val state: StateFlow<EventState>
        field = MutableStateFlow(EventState())

    init {
        loadEvents()
    }

    fun likeById(id: Long, isLiked: Boolean) {
        eventRepository.likeById(
            id,
            isLiked,
            object : Callback<Event> {
                override fun onSuccess(data: Event) {
                    state.update {
                        it.copy(
                            events = state.value.events.map {
                                if (it.id == data.id) {
                                    data
                                } else {
                                    it
                                }
                            },
                            status = Status.Idle,
                        )
                    }
                    createEventsByDate(state.value.events)
                }

                override fun onError(throwable: Throwable) {
                    state.update {
                        it.copy(
                            status = Status.Error(throwable),
                        )
                    }
                }
            }
        )
    }

    fun participateById(id: Long, isParticipated: Boolean) {
        eventRepository.participateById(
            id,
            isParticipated,
            object : Callback<Event> {
                override fun onSuccess(data: Event) {
                    state.update {
                        it.copy(
                            events = state.value.events.map {
                                if (it.id == data.id) {
                                    data
                                } else {
                                    it
                                }
                            },
                            status = Status.Idle,
                        )
                    }
                    createEventsByDate(state.value.events)
                }

                override fun onError(throwable: Throwable) {
                    state.update {
                        it.copy(
                            status = Status.Error(throwable),
                        )
                    }
                }
            }
        )
    }

    fun deleteEvent(id: Long) {
        eventRepository.deleteById(
            id,
            object : Callback<Unit> {
                override fun onSuccess(data: Unit) {
                    state.update {
                        it.copy(
                            events = state.value.events.filter { it.id != id },
                            status = Status.Idle,
                        )
                    }
                    createEventsByDate(state.value.events)
                }

                override fun onError(throwable: Throwable) {
                    state.update {
                        it.copy(
                            status = Status.Error(throwable),
                        )
                    }
                }
            }
        )
    }

    fun loadEvents() {
        state.update { it.copy(status = Status.Loading) }
        eventRepository.getEvents(
            object : Callback<List<Event>> {
                override fun onSuccess(data: List<Event>) {
                    state.update {
                        it.copy(
                            events = data,
                            status = Status.Idle,
                        )
                    }
                    createEventsByDate(state.value.events)
                }

                override fun onError(throwable: Throwable) {
                    state.update {
                        it.copy(
                            status = Status.Error(throwable),
                        )
                    }
                }
            }
        )
    }

    fun consumerError() {
        state.update {
            it.copy(
                status = Status.Idle,
            )
        }
    }

    private fun createEventsByDate(updatedEvents: List<Event>) {
        state.update { state ->
            val groupedEvents = DateSeparators.groupByDate(
                items = updatedEvents,
            )
            state.copy(events = updatedEvents, eventsByDate = groupedEvents)
        }
    }
}
