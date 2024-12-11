package com.eltex.androidschool.view.fragment.newevent

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
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class NewEventViewModel(
    private val eventRepository: EventRepository,
) : ViewModel() {
    val state: StateFlow<NewEventState>
        field = MutableStateFlow(NewEventState())

    fun setAttachment(uri: Uri) {
        state.update {
            it.copy(
                attachment = Attachment(
                    url = uri.toString(),
                    type = AttachmentType.IMAGE,
                ),
            )
        }
    }

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
                link = text ?: Uri.parse(text)?.toString(),
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
        if (textContent.isEmpty() && state.value.attachment == null) {
            return
        }
        eventRepository.saveEvent(
            id = 0,
            content = textContent,
            dateTime = state.value.dateTime,
            attachment = state.value.attachment,
            link = state.value.link,
            object : Callback<Event> {
                override fun onSuccess(data: Event) {
                    state.update {
                        it.copy(
                            textContent = data.content,
                            attachment = data.attachment,
                            dateTime = data.datetime,
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