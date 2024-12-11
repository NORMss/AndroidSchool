package com.eltex.androidschool.view.fragment.editevent

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.eltex.androidschool.domain.model.Attachment
import com.eltex.androidschool.domain.model.AttachmentType
import com.eltex.androidschool.domain.model.Event
import com.eltex.androidschool.domain.repository.EventRepository
import com.eltex.androidschool.utils.remote.Callback
import com.eltex.androidschool.view.common.Status
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class EditEventViewModel(
    private val eventRepository: EventRepository,
    eventId: Long,
) : ViewModel() {
    val state: StateFlow<EditEventState>
        field = MutableStateFlow(EditEventState())

    init {
        getEvent(eventId)
    }

    fun setAttachment(uri: Uri) {
        state.update {
            it.copy(
                event = state.value.event.copy(
                    attachment = Attachment(
                        url = uri.toString(),
                        type = AttachmentType.IMAGE,
                    )
                )
            )
        }
    }

    fun setText(text: String) {
        state.update {
            it.copy(
                event = state.value.event.copy(
                    content = text,
                )
            )
        }
    }

    fun setLink(text: String) {
        state.update {
            it.copy(
                event = state.value.event.copy(
                    link = text,
                )
            )
        }
    }

    private fun getEvent(id: Long) {
        eventRepository.getEvents(
            object : Callback<List<Event>> {
                override fun onSuccess(data: List<Event>) {
                    state.update {
                        it.copy(
                            event = data.filter { it.id == id }.first(),
                            status = Status.Idle,
                        )
                    }
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

    fun editEvent() {
        eventRepository.saveEvent(
            event = state.value.event,
            callback = object : Callback<Event> {
                override fun onSuccess(data: Event) {
                    state.update {
                        it.copy(
                            event = data,
                            status = Status.Idle,
                        )
                    }
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
}