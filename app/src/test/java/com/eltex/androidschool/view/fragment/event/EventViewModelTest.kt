package com.eltex.androidschool.view.fragment.event

import com.eltex.androidschool.TestCoroutineRule
import com.eltex.androidschool.domain.mapper.GroupByDateMapper
import com.eltex.androidschool.domain.model.Event
import com.eltex.androidschool.model.TestEvent
import com.eltex.androidschool.repository.TestErrorEventRepository
import com.eltex.androidschool.view.common.Status
import com.eltex.androidschool.view.model.EventUi
import com.eltex.androidschool.view.util.datetime.DateSeparators
import junit.framework.TestCase.assertEquals
import org.junit.Rule
import org.junit.Test

class EventViewModelTest {

    @get:Rule
    val coroutineRule = TestCoroutineRule()

    @Test
    fun `likeById error then state contains error`() {
        val error = RuntimeException("Like failed")
        val eventRepository = object : TestErrorEventRepository {
            override suspend fun likeById(id: Long, isLiked: Boolean): Event = throw error
        }
        val viewModel = EventViewModel(
            eventRepository = eventRepository,
            mapper = object : GroupByDateMapper<Event, EventUi> {
                override fun map(from: List<Event>): List<DateSeparators.GroupByDate<EventUi>> =
                    emptyList()
            },
        )
        viewModel.likeById(1L, true)

        val result = viewModel.state.value.status
        val equals = Status.Error(error)

        assertEquals(result, equals)
    }

    @Test
    fun `participateById error then state contains error`() {
        val error = RuntimeException("Like failed")
        val eventRepository = object : TestErrorEventRepository {
            override suspend fun participateById(id: Long, isParticipate: Boolean): Event =
                throw error
        }
        val viewModel = EventViewModel(
            eventRepository = eventRepository,
            mapper = object : GroupByDateMapper<Event, EventUi> {
                override fun map(from: List<Event>): List<DateSeparators.GroupByDate<EventUi>> =
                    emptyList()
            },
        )
        viewModel.participateById(1L, true)

        val result = viewModel.state.value.status
        val equals = Status.Error(error)

        assertEquals(result, equals)
    }

    @Test
    fun `deleteById error then state contains error`() {
        val error = RuntimeException("Delete failed")
        val eventRepository = object : TestErrorEventRepository {
            override suspend fun deleteById(id: Long) = throw error
        }
        val mapper = object : GroupByDateMapper<Event, EventUi> {
            override fun map(from: List<Event>): List<DateSeparators.GroupByDate<EventUi>> =
                emptyList()
        }
        val viewModel = EventViewModel(
            eventRepository = eventRepository,
            mapper = mapper,
        )

        viewModel.deleteEvent(1L)

        val result = viewModel.state.value.status
        val equals = Status.Error(error)

        assertEquals(result, equals)
    }

    @Test
    fun `loadEvents error then state contains error`() {
        val error = RuntimeException("get posts failed")
        val eventRepository = object : TestErrorEventRepository {
            override suspend fun getEvents(): List<Event> = throw error
        }
        val mapper = object : GroupByDateMapper<Event, EventUi> {
            override fun map(from: List<Event>): List<DateSeparators.GroupByDate<EventUi>> =
                emptyList()
        }
        val viewModel = EventViewModel(
            eventRepository = eventRepository,
            mapper = mapper,
        )

        viewModel.loadEvents()

        val result = viewModel.state.value.status
        val equals = Status.Error(error)

        assertEquals(result, equals)
    }

    @Test
    fun `participateById success`() {
        val eventRepository = object : TestErrorEventRepository {
            override suspend fun getEvents(): List<Event> =
                listOf(
                    TestEvent(1).toDomainEvent(),
                    TestEvent(2).toDomainEvent()
                )

            override suspend fun participateById(id: Long, isParticipate: Boolean): Event =
                TestEvent(1).toDomainEvent()
        }
        val viewModel = EventViewModel(
            eventRepository = eventRepository,
            mapper = object : GroupByDateMapper<Event, EventUi> {
                override fun map(from: List<Event>): List<DateSeparators.GroupByDate<EventUi>> =
                    emptyList()
            },
        )
        viewModel.participateById(1L, true)

        val result = viewModel.state.value.status
        val equals = Status.Idle

        assertEquals(result, equals)
    }


    @Test
    fun `likeById success`() {
        val eventRepository = object : TestErrorEventRepository {
            override suspend fun getEvents(): List<Event> =
                listOf(
                    TestEvent(1).toDomainEvent(),
                    TestEvent(2).toDomainEvent()
                )

            override suspend fun likeById(id: Long, isLiked: Boolean): Event =
                TestEvent(1).toDomainEvent()
        }
        val viewModel = EventViewModel(
            eventRepository = eventRepository,
            mapper = object : GroupByDateMapper<Event, EventUi> {
                override fun map(from: List<Event>): List<DateSeparators.GroupByDate<EventUi>> =
                    emptyList()
            },
        )
        viewModel.likeById(1L, true)

        val result = viewModel.state.value.status
        val equals = Status.Idle

        assertEquals(result, equals)
    }


    @Test
    fun `deleteById success`() {
        val eventRepository = object : TestErrorEventRepository {}
        val viewModel = EventViewModel(
            eventRepository = eventRepository,
            mapper = object : GroupByDateMapper<Event, EventUi> {
                override fun map(from: List<Event>): List<DateSeparators.GroupByDate<EventUi>> =
                    emptyList()
            },
        )
        viewModel.deleteEvent(1L)

        val result = viewModel.state.value.status
        val equals = Status.Idle

        assertEquals(result, equals)
    }


    @Test
    fun `loadEvents success`() {
        val eventRepository = object : TestErrorEventRepository {
            override suspend fun getEvents(): List<Event> =
                listOf(TestEvent(1L).toDomainEvent())
        }
        val viewModel = EventViewModel(
            eventRepository = eventRepository,
            mapper = object : GroupByDateMapper<Event, EventUi> {
                override fun map(from: List<Event>): List<DateSeparators.GroupByDate<EventUi>> =
                    emptyList()
            },
        )
        viewModel.loadEvents()

        val result = viewModel.state.value.status
        val equals = Status.Idle

        assertEquals(result, equals)
    }

    @Test
    fun `consumerError clear status`() {
        val error = RuntimeException("Like failed")
        val eventRepository = object : TestErrorEventRepository {
            override suspend fun likeById(id: Long, isLiked: Boolean): Event = throw error
        }
        val viewModel = EventViewModel(
            eventRepository = eventRepository,
            mapper = object : GroupByDateMapper<Event, EventUi> {
                override fun map(from: List<Event>): List<DateSeparators.GroupByDate<EventUi>> =
                    emptyList()
            },
        )
        viewModel.likeById(1L, true)

        val resultWithError = viewModel.state.value.status
        val equalsWithError = Status.Error(error)

        assertEquals(resultWithError, equalsWithError)

        viewModel.consumerError()

        val result = viewModel.state.value.status
        val equals = Status.Idle

        assertEquals(result, equals)
    }

}