package com.eltex.androidschool.view.fragment.newevent

import com.eltex.androidschool.domain.model.Event
import com.eltex.androidschool.model.TestEvent
import com.eltex.androidschool.repository.TestErrorEventRepository
import com.eltex.androidschool.view.common.Status
import kotlinx.datetime.Instant
import org.junit.Assert.*
import org.junit.Test

class NewEventViewModelTest {
    @Test
    fun `addEvent error then state contains error`() {
        val error = RuntimeException("Save failed")
        val eventRepository = object : TestErrorEventRepository {
            override suspend fun saveEvent(event: Event): Event = throw error
        }
        val viewModel = NewEventViewModel(
            eventRepository = eventRepository,
        )

        viewModel.setText("test")
        viewModel.addEvent()

        val equals = Status.Error(error)
        val result = viewModel.state.value.status

        assertEquals(equals, result)
    }

    @Test
    fun `addEvent success`() {
        val eventRepository = object : TestErrorEventRepository {
            override suspend fun saveEvent(event: Event): Event =
                TestEvent(id = 1L, content = "test").toDomainEvent()
        }
        val viewModel = NewEventViewModel(
            eventRepository = eventRepository,
        )

        viewModel.setDateTime(Instant.fromEpochSeconds(0).toString())
        viewModel.addEvent()

        val equals = Status.Idle
        val result = viewModel.state.value.status

        assertEquals(equals, result)
    }


    @Test
    fun setTextTest() {
        val eventRepository = object : TestErrorEventRepository {}
        val viewModel = NewEventViewModel(
            eventRepository = eventRepository,
        )

        val testText = "test"

        viewModel.setText(testText)

        val equals = testText
        val result = viewModel.state.value.textContent

        assertEquals(equals, result)
    }

    @Test
    fun setLinkTest() {
        val eventRepository = object : TestErrorEventRepository {}
        val viewModel = NewEventViewModel(
            eventRepository = eventRepository,
        )
        val testLink = "example.com"

        viewModel.setLink(testLink)

        val equals = testLink
        val result = viewModel.state.value.link

        assertEquals(equals, result)
    }
}