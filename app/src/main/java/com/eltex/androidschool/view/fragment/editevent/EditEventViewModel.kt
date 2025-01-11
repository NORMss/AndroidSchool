package com.eltex.androidschool.view.fragment.editevent

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eltex.androidschool.domain.model.Attachment
import com.eltex.androidschool.domain.model.AttachmentType
import com.eltex.androidschool.domain.repository.EventRepository
import com.eltex.androidschool.view.common.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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

    fun editEvent() {
        try {
            viewModelScope.launch {
                val event = eventRepository.saveEvent(state.value.event)
                state.update {
                    it.copy(
                        event = event,
                        status = Status.Idle,
                    )
                }
            }
        } catch (e: Exception) {
            state.update {
                it.copy(
                    status = Status.Error(e),
                )
            }
        }
    }

    private fun getEvent(id: Long) {
        viewModelScope.launch {
            try {
                val event = eventRepository.getEvents().filter { it.id == id }.first()
                state.update {
                    it.copy(
                        event = event,
                        status = Status.Idle,
                    )
                }
            } catch (e: Exception) {
                state.update {
                    it.copy(
                        status = Status.Error(e),
                    )
                }
            }
        }
    }
}