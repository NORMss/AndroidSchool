package com.eltex.androidschool.view.fragment.editevent

import com.eltex.androidschool.TestCoroutineRule
import com.eltex.androidschool.TestSchedulersProvider
import com.eltex.androidschool.domain.model.Event
import com.eltex.androidschool.model.TestEvent
import com.eltex.androidschool.repository.TestErrorEventRepository
import com.eltex.androidschool.view.common.Status
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class EditEventViewModelTest {

    @get:Rule
    val coroutineRule = TestCoroutineRule()

    @Test
    fun setTextTest() {
        val eventRepository = object : TestErrorEventRepository {}
        val viewModel = EditEventViewModel(
            eventRepository = eventRepository,
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
        val eventRepository = object : TestErrorEventRepository {
            override suspend fun saveEvent(event: Event): Event =
                TestEvent(id = 1L, content = event.content).toDomainEvent()
        }
        val viewModel = EditEventViewModel(
            eventRepository = eventRepository,
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
        val eventRepository = object : TestErrorEventRepository {
            override suspend fun saveEvent(event: Event): Event = throw error
        }
        val viewModel = EditEventViewModel(
            eventRepository = eventRepository,
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
        val eventRepository = object : TestErrorEventRepository {}
        val viewModel = EditEventViewModel(
            eventRepository = eventRepository,
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