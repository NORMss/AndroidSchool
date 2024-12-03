package com.eltex.androidschool.view.fragment.newevent

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eltex.androidschool.domain.model.Attachment
import com.eltex.androidschool.domain.model.AttachmentType
import com.eltex.androidschool.domain.repository.EventRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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

    fun setLink(text: String) {
        state.update {
            it.copy(
                link = Uri.parse(text)?.toString(),
            )
        }
    }

    fun addPost() {
        val textContent = state.value.textContent.trim()
        if (textContent.isEmpty() && state.value.attachment == null) {
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                eventRepository.addEvent(
                    textContent = textContent,
                    attachment = state.value.attachment,
                    link = state.value.link,
                )
                state.update { NewEventState() }
            } catch (e: Exception) {
                Log.e("NewPostViewModel", "Failed to add post", e)
            }
        }
    }
}