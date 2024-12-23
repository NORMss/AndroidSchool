package com.eltex.androidschool.view.fragment.event

import androidx.lifecycle.ViewModel
import com.eltex.androidschool.domain.mapper.GroupByDateMapper
import com.eltex.androidschool.domain.model.Event
import com.eltex.androidschool.domain.repository.EventRepository
import com.eltex.androidschool.domain.rx.SchedulersProvider
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
    private val schedulersProvider: SchedulersProvider = SchedulersProvider.DEFAULT,
) : ViewModel() {
    private val disposable = CompositeDisposable()

    val state: StateFlow<EventState>
        field = MutableStateFlow(EventState())

    init {
        loadEvents()
    }

    fun likeById(id: Long, isLiked: Boolean) {
        eventRepository.likeById(id = id, isLiked = isLiked)
            .observeOn(schedulersProvider.io)
            .map { updatedEvent ->
                val updatedEvents = state.value.events.map { event ->
                    if (event.id == updatedEvent.id) updatedEvent else event
                }
                Pair(
                    updatedEvents,
                    mapper.map(updatedEvents)
                )
            }
            .observeOn(schedulersProvider.mainThread)
            .subscribeBy(
                onSuccess = { (updatedEvents, eventsByDate) ->
                    state.update {
                        it.copy(
                            events = updatedEvents,
                            eventsByDate = eventsByDate,
                            status = Status.Idle,
                        )
                    }
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
        eventRepository.participateById(id = id, isParticipated = isParticipated)
            .observeOn(schedulersProvider.io)
            .map { updatedEvent ->
                val updatedEvents = state.value.events.map { event ->
                    if (event.id == updatedEvent.id) updatedEvent else event
                }
                Pair(
                    updatedEvents,
                    mapper.map(updatedEvents)
                )
            }
            .observeOn(schedulersProvider.mainThread)
            .subscribeBy(
                onSuccess = { (updatedEvents, eventsByDate) ->
                    state.update {
                        it.copy(
                            events = updatedEvents,
                            eventsByDate = eventsByDate,
                            status = Status.Idle,
                        )
                    }
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
        eventRepository.deleteById(id)
            .observeOn(schedulersProvider.io)
            .map {
                val updatedEvents = state.value.events.filter { it.id != id }
                Pair(
                    updatedEvents,
                    mapper.map(updatedEvents)
                )
            }
            .observeOn(schedulersProvider.mainThread)
            .subscribeBy(
                onSuccess = { (updatedEvents, eventsByDate) ->
                    state.update {
                        it.copy(
                            events = updatedEvents,
                            eventsByDate = eventsByDate,
                            status = Status.Idle,
                        )
                    }

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
        eventRepository.getEvents()
            .observeOn(schedulersProvider.io)
            .map { updatedEvents ->
                Pair(
                    updatedEvents,
                    mapper.map(updatedEvents)
                )
            }
            .observeOn(schedulersProvider.mainThread)
            .subscribeBy(
                onSuccess = { (updatedEvents, eventsByDate) ->
                    state.update {
                        it.copy(
                            events = updatedEvents,
                            eventsByDate = eventsByDate,
                            status = Status.Idle,
                        )
                    }
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

    override fun onCleared() {
        disposable.dispose()
    }

}
