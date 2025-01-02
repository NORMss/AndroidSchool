package com.eltex.androidschool.view.fragment.editevent

import com.eltex.androidschool.TestSchedulersProvider
import com.eltex.androidschool.domain.repository.EventRepository
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
    fun editEventTest() {
        val eventRepository = object : EventRepository {}
        val viewModel = EditEventViewModel(
            eventRepository = eventRepository,
            schedulersProvider = TestSchedulersProvider,
            eventId = 1L,
        )
        val testText = "test"

        viewModel.setText(testText)

        viewModel.editEvent()

        val equals = testText
        val result = viewModel.state.value.event.content

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