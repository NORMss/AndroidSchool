package com.eltex.androidschool.view.fragment.editpost

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eltex.androidschool.domain.repository.PostRepository
import com.eltex.androidschool.view.common.Status
import com.eltex.androidschool.view.model.FileModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = EditPostViewModel.EditPostViewModelFactory::class)
class EditPostViewModel @AssistedInject constructor(
    private val postRepository: PostRepository,
    @Assisted private val postId: Long,
) : ViewModel() {
    val state: StateFlow<EditPostState>
        field = MutableStateFlow(EditPostState())

    init {
        getPost(postId)
    }

    fun setText(text: String) {
        state.update {
            it.copy(
                post = state.value.post.copy(
                    content = text,
                )
            )
        }
    }

    fun editPost() {
        viewModelScope.launch {
            try {
                val updatedPost = postRepository.savePost(
                    id = state.value.post.id,
                    content = state.value.post.content,
                    fileModel = state.value.post.attachment?.let {
                        FileModel(
                            Uri.parse(it.url),
                            it.type
                        )
                    },
                )
                state.update {
                    it.copy(
                        result = updatedPost,
                        post = updatedPost,
                        status = Status.Idle,
                    )
                }
            } catch (e: Exception) {
                state.update {
                    it.copy(
                        status = Status.Error(e)
                    )
                }
            }
        }
    }

    private fun getPost(id: Long) {
        state.update { it.copy(status = Status.Loading) }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val post = postRepository.getPosts().filter { it.id == id }.first()
                state.update {
                    it.copy(
                        post = post,
                        status = Status.Idle,
                    )
                }
            } catch (e: Exception) {
                state.update {
                    it.copy(
                        status = Status.Error(e)
                    )
                }
            }
        }
    }

    @AssistedFactory
    interface EditPostViewModelFactory {
        fun create(id: Long): EditPostViewModel
    }
}
