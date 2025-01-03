package com.eltex.androidschool.view.fragment.newevent

import com.eltex.androidschool.TestSchedulersProvider
import com.eltex.androidschool.domain.model.Event
import com.eltex.androidschool.domain.repository.EventRepository
import com.eltex.androidschool.model.TestEvent
import com.eltex.androidschool.view.common.Status
import io.reactivex.rxjava3.core.Single
import kotlinx.datetime.Instant
import org.junit.Assert.*
import org.junit.Test

class NewEventViewModelTest {
    @Test
    fun `addEvent error then state contains error`() {
        val error = RuntimeException("Save failed")
        val eventRepository = object : EventRepository {
            override fun saveEvent(event: Event): Single<Event> =
                Single.error(error)
        }
        val viewModel = NewEventViewModel(
            eventRepository = eventRepository,
            schedulersProvider = TestSchedulersProvider,
        )

        viewModel.setText("test")
        viewModel.addEvent()

        val equals = Status.Error(error)
        val result = viewModel.state.value.status

        assertEquals(equals, result)
    }

    @Test
    fun `addEvent success`() {
        val eventRepository = object : EventRepository {
            override fun saveEvent(event: Event): Single<Event> =
                Single.fromCallable { TestEvent(id = 1L, content = "test").toDomainEvent() }
        }
        val viewModel = NewEventViewModel(
            eventRepository = eventRepository,
            schedulersProvider = TestSchedulersProvider,
        )

        viewModel.setDateTime(Instant.fromEpochSeconds(0).toString())
        viewModel.addEvent()

        val equals = Status.Idle
        val result = viewModel.state.value.status

        assertEquals(equals, result)
    }


    @Test
    fun setTextTest() {
        val eventRepository = object : EventRepository {}
        val viewModel = NewEventViewModel(
            eventRepository = eventRepository,
            schedulersProvider = TestSchedulersProvider,
        )

        val testText = "test"

        viewModel.setText(testText)

        val equals = testText
        val result = viewModel.state.value.textContent

        assertEquals(equals, result)
    }

    @Test
    fun setLinkTest() {
        val eventRepository = object : EventRepository {}
        val viewModel = NewEventViewModel(
            eventRepository = eventRepository,
            schedulersProvider = TestSchedulersProvider,
        )
        val testLink = "example.com"

        viewModel.setLink(testLink)

        val equals = testLink
        val result = viewModel.state.value.link

        assertEquals(equals, result)
    }
}