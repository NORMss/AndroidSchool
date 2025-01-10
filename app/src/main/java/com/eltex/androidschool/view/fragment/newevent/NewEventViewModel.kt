package com.eltex.androidschool.view.fragment.newevent

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eltex.androidschool.domain.model.Attachment
import com.eltex.androidschool.domain.model.AttachmentType
import com.eltex.androidschool.domain.model.Coordinates
import com.eltex.androidschool.domain.model.Event
import com.eltex.androidschool.domain.model.EventType
import com.eltex.androidschool.domain.repository.EventRepository
import com.eltex.androidschool.view.common.Status
import kotlinx.coroutines.Dispatchers
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
        if (textContent.isEmpty() && state.value.attachment == null) {
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                eventRepository.saveEvent(
                    event = Event(
                        id = 0,
                        authorId = 0,
                        author = "",
                        authorJob = "",
                        authorAvatar = "",
                        content = state.value.textContent,
                        datetime = state.value.dateTime,
                        published = Instant.fromEpochSeconds(0),
                        coords = Coordinates(
                            lat = 54.9833,
                            long = 82.8964,
                        ),
                        type = EventType.OFFLINE,
                        likeOwnerIds = emptySet(),
                        likedByMe = false,
                        speakerIds = emptySet(),
                        participantsIds = emptySet(),
                        participatedByMe = false,
                        attachment = state.value.attachment,
                        link = state.value.link,
                        users = emptyMap(),
                    ),
                ).let { data ->
                    state.update {
                        it.copy(
                            textContent = data.content,
                            attachment = data.attachment,
                            dateTime = data.datetime,
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
    }
}