package com.eltex.androidschool.view.activity.post

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eltex.androidschool.domain.model.Attachment
import com.eltex.androidschool.domain.model.AttachmentType
import com.eltex.androidschool.domain.repository.PostRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewPostViewModel(
    private val postRepository: PostRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(NewPostState())
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

    fun addPost() {
        val textContent = _state.value.textContent.trim()
        if (textContent.isEmpty() && _state.value.attachment == null) {
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                postRepository.addPost(
                    textContent = textContent,
                    attachment = _state.value.attachment,
                )
                _state.update { NewPostState() }
            } catch (e: Exception) {
                Log.e("NewPostViewModel", "Failed to add post", e)
            }
        }
    }
}
