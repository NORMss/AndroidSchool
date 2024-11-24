package com.eltex.androidschool.view.activity.event

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eltex.androidschool.domain.model.Attachment
import com.eltex.androidschool.domain.model.AttachmentType
import com.eltex.androidschool.domain.repository.EventRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewEventViewModel(
    private val eventRepository: EventRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(NewEventState())
    val state = _state.asStateFlow()

    fun setAttachment(uri: Uri) {
        _state.update {
            it.copy(
                attachment = Attachment(
                    url = uri.toString(),
                    type = AttachmentType.IMAGE,
                ),
            )
        }
    }

    fun setText(text: String) {
        _state.update {
            it.copy(
                textContent = text,
            )
        }
    }

    fun setLink(text: String) {
        _state.update {
            it.copy(
                link = Uri.parse(text)?.toString(),
            )
        }
    }

    fun addPost() {
        val textContent = _state.value.textContent.trim()
        if (textContent.isEmpty() && _state.value.attachment == null) {
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                eventRepository.addEvent(
                    textContent = textContent,
                    attachment = _state.value.attachment,
                    link = _state.value.link,
                )
                _state.update { NewEventState() }
            } catch (e: Exception) {
                Log.e("NewPostViewModel", "Failed to add post", e)
            }
        }
    }
}