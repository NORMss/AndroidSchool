package com.eltex.androidschool.view.fragment.newevent

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
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class NewEventViewModel(
    private val eventRepository: EventRepository,
) : ViewModel() {
    val state: StateFlow<NewEventState>
        field = MutableStateFlow(NewEventState())

    fun setText(text: String) {
        state.update {
            it.copy(
                textContent = text,
            )
        }
    }

    fun setLink(text: String?) {
        state.update {
            it.copy(
                link = text ?: Uri.parse(text).toString(),
            )
        }
    }

    fun setDateTime(text: String?) {
        state.update {
            it.copy(
                dateTime = text?.let { Instant.parse(it) } ?: Clock.System.now()
            )
        }
    }

    fun addEvent() {
        val textContent = state.value.textContent.trim()
        if (textContent.isEmpty() && state.value.file == null) {
            return
        }
        viewModelScope.launch {
            try {
                state.update {
                    it.copy(
                        result = eventRepository.saveEvent(
                            id = 0,
                            content = state.value.textContent,
                            link = state.value.link,
                            date = state.value.dateTime,
                            fileModel = state.value.file,
                        ).also { data ->
                            state.update {
                                it.copy(
                                    textContent = data.content,
                                    dateTime = data.datetime,
                                    status = Status.Idle,
                                )
                            }
                        }
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

    private fun getAttachment(): Attachment? {
        return state.value.file?.let {
            Attachment(
                url = it.uri.toString(),
                type = AttachmentType.IMAGE
            )
        }
    }

    fun setFile(fileModel: FileModel?) {
        state.update {
            it.copy(
                file = fileModel
            )
        }
    }

    fun removeFile() {
        state.update {
            it.copy(
                file = null
            )
        }
    }

    fun consumerError() {
        state.update {
            it.copy(
                status = Status.Idle,
            )
        }
    }
}