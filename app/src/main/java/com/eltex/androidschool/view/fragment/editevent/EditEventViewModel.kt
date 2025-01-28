package com.eltex.androidschool.view.fragment.editevent

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eltex.androidschool.domain.model.Attachment
import com.eltex.androidschool.domain.model.AttachmentType
import com.eltex.androidschool.domain.repository.EventRepository
import com.eltex.androidschool.view.common.Status
import com.eltex.androidschool.view.model.FileModel
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
        viewModelScope.launch {
            try {
                val event = eventRepository.saveEvent(
                    id = 0,
                    content = state.value.event.content,
                    link = state.value.event.link,
                    date = state.value.event.datetime,
                    fileModel = state.value.event.attachment?.let {
                        FileModel(
                            Uri.parse(it.url),
                            it.type,
                        )
                    },
                )
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