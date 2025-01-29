package com.eltex.androidschool.view.fragment.newpost

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eltex.androidschool.domain.repository.PostRepository
import com.eltex.androidschool.view.common.Status
import com.eltex.androidschool.view.model.FileModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewPostViewModel(
    private val postRepository: PostRepository,
) : ViewModel() {

    val state: StateFlow<NewPostState>
        field = MutableStateFlow(NewPostState())

    fun setText(text: String) {
        state.update {
            it.copy(
                textContent = text,
            )
        }
    }

    fun addPost() {
        val textContent = state.value.textContent.trim()
        if (textContent.isEmpty()) {
            return
        }
        viewModelScope.launch {
            try {
                state.update {
                    it.copy(
                        result = postRepository.savePost(
                            id = 0,
                            content = state.value.textContent,
                            fileModel = state.value.file,
                        ).also { data ->
                            state.update {
                                it.copy(
                                    textContent = data.content,
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
