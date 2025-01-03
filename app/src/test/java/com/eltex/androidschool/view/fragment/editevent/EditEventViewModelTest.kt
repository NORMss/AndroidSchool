package com.eltex.androidschool.view.fragment.editevent

import com.eltex.androidschool.TestSchedulersProvider
import com.eltex.androidschool.domain.model.Event
import com.eltex.androidschool.domain.repository.EventRepository
import com.eltex.androidschool.model.TestEvent
import com.eltex.androidschool.view.common.Status
import io.reactivex.rxjava3.core.Single
import org.junit.Assert.assertEquals
import org.junit.Test

class EditEventViewModelTest {
    @Test
    fun setTextTest() {
        val eventRepository = object : EventRepository {}
        val viewModel = EditEventViewModel(
            eventRepository = eventRepository,
            schedulersProvider = TestSchedulersProvider,
            eventId = 1L,
        )

        val testText = "test"

        viewModel.setText(testText)

        val equals = testText
        val result = viewModel.state.value.event.content

        assertEquals(equals, result)
    }

    @Test
    fun `editEventTest success`() {
        val eventRepository = object : EventRepository {
            override fun saveEvent(event: Event): Single<Event> =
                Single.fromCallable { TestEvent(id = 1L, content = event.content).toDomainEvent() }
        }
        val viewModel = EditEventViewModel(
            eventRepository = eventRepository,
            schedulersProvider = TestSchedulersProvider,
            eventId = 1L,
        )
        val testText = "testText"

        viewModel.setText(testText)

        viewModel.editEvent()

        val equals = testText
        val result = viewModel.state.value.event.content

        assertEquals(equals, result)
    }

    @Test
    fun `editEventTest error then state contains error`() {
        val error = RuntimeException("Save failed")
        val eventRepository = object : EventRepository {
            override fun saveEvent(event: Event): Single<Event> =
                Single.error(error)
        }
        val viewModel = EditEventViewModel(
            eventRepository = eventRepository,
            schedulersProvider = TestSchedulersProvider,
            eventId = 1L,
        )
        val testText = "testText"

        viewModel.setText(testText)

        viewModel.editEvent()

        val equals = Status.Error(error)
        val result = viewModel.state.value.status

        assertEquals(equals, result)
    }

    @Test
    fun setLinkTest() {
        val eventRepository = object : EventRepository {}
        val viewModel = EditEventViewModel(
            eventRepository = eventRepository,
            schedulersProvider = TestSchedulersProvider,
            eventId = 1L,
        )
        val testLink = "example.com"

        viewModel.setLink(testLink)

        viewModel.editEvent()

        val equals = testLink
        val result = viewModel.state.value.event.link

        assertEquals(equals, result)
    }
}