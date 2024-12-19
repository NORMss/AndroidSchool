package com.eltex.androidschool.view.fragment.event

import androidx.lifecycle.ViewModel
import com.eltex.androidschool.domain.mapper.GroupByDateMapper
import com.eltex.androidschool.domain.model.Event
import com.eltex.androidschool.domain.repository.EventRepository
import com.eltex.androidschool.view.common.Status
import com.eltex.androidschool.view.model.EventUi
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class EventViewModel(
    private val eventRepository: EventRepository,
    private val mapper: GroupByDateMapper<Event, EventUi>,
) : ViewModel() {
    val disposable = CompositeDisposable()

    val state: StateFlow<EventState>
        field = MutableStateFlow(EventState())

    init {
        loadEvents()
    }

    fun likeById(id: Long, isLiked: Boolean) {
        eventRepository.likeById(id = id, isLiked = isLiked).subscribeBy(
            onSuccess = { events ->
                state.update {
                    it.copy(
                        events = state.value.events.map {
                            if (it.id == events.id) {
                                events
                            } else {
                                it
                            }
                        },
                        status = Status.Idle,
                    )
                }
                createEventsByDate(state.value.events)
            },
            onError = { throwable ->
                state.update {
                    it.copy(
                        status = Status.Error(throwable),
                    )
                }
            }
        ).addTo(disposable)
    }

    fun participateById(id: Long, isParticipated: Boolean) {
        eventRepository.participateById(id = id, isParticipated = isParticipated).subscribeBy(
            onSuccess = { events ->
                state.update {
                    it.copy(
                        events = state.value.events.map {
                            if (it.id == events.id) {
                                events
                            } else {
                                it
                            }
                        },
                        status = Status.Idle,
                    )
                }
                createEventsByDate(state.value.events)
            },
            onError = { throwable ->
                state.update {
                    it.copy(
                        status = Status.Error(throwable),
                    )
                }

            }
        ).addTo(disposable)
    }

    fun deleteEvent(id: Long) {
        eventRepository.deleteById(id).subscribeBy(
            onComplete = {
                state.update {
                    it.copy(
                        events = state.value.events.filter { it.id != id },
                        status = Status.Idle,
                    )
                }
                createEventsByDate(state.value.events)
            },
            onError = { throwable ->
                state.update {
                    it.copy(
                        status = Status.Error(throwable),
                    )
                }

            }
        ).addTo(disposable)
    }

    fun loadEvents() {
        state.update { it.copy(status = Status.Loading) }
        eventRepository.getEvents().subscribeBy(
            onSuccess = { events ->
                state.update {
                    it.copy(
                        events = events,
                        status = Status.Idle,
                    )
                }
                createEventsByDate(state.value.events)
            },
            onError = { throwable ->
                state.update {
                    it.copy(
                        status = Status.Error(throwable),
                    )
                }

            }
        ).addTo(disposable)
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
//            val groupedEvents = DateSeparators.groupByDate(
//                items = updatedEvents,
//            )
            state.copy(events = updatedEvents, eventsByDate = mapper.map(updatedEvents))
        }
    }

    override fun onCleared() {
        disposable.dispose()
    }

}
