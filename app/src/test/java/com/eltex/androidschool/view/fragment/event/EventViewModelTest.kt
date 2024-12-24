package com.eltex.androidschool.view.fragment.event

import com.eltex.androidschool.TestSchedulersProvider
import com.eltex.androidschool.domain.mapper.GroupByDateMapper
import com.eltex.androidschool.domain.model.Event
import com.eltex.androidschool.domain.repository.EventRepository
import com.eltex.androidschool.view.common.Status
import com.eltex.androidschool.view.model.EventUi
import com.eltex.androidschool.view.util.datetime.DateSeparators
import io.reactivex.rxjava3.core.Single
import junit.framework.TestCase.assertEquals
import org.junit.Test

class EventViewModelTest {
    @Test
    fun `likeById error then state contains error`() {
        val error = RuntimeException("Like failed")
        val eventRepository = object : EventRepository {
            override fun likeById(id: Long, isLiked: Boolean): Single<Event> =
                Single.error(error)
        }
        val viewModel = EventViewModel(
            eventRepository = eventRepository,
            mapper = object : GroupByDateMapper<Event, EventUi> {
                override fun map(from: List<Event>): List<DateSeparators.GroupByDate<EventUi>> =
                    emptyList()
            },
            schedulersProvider = TestSchedulersProvider
        )
        viewModel.likeById(1L, true)

        val result = viewModel.state.value.status
        val equals = Status.Error(error)

        assertEquals(result, equals)
    }

    @Test
    fun `participateById error then state contains error`() {
        val error = RuntimeException("Like failed")
        val eventRepository = object : EventRepository {
            override fun participateById(id: Long, isParticipate: Boolean): Single<Event> =
                Single.error(error)
        }
        val viewModel = EventViewModel(
            eventRepository = eventRepository,
            mapper = object : GroupByDateMapper<Event, EventUi> {
                override fun map(from: List<Event>): List<DateSeparators.GroupByDate<EventUi>> =
                    emptyList()
            },
            schedulersProvider = TestSchedulersProvider
        )
        viewModel.participateById(1L, true)

        val result = viewModel.state.value.status
        val equals = Status.Error(error)

        assertEquals(result, equals)
    }

}