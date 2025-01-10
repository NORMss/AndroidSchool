package com.eltex.androidschool.view.fragment.editpost

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eltex.androidschool.domain.repository.PostRepository
import com.eltex.androidschool.view.common.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditPostViewModel(
    private val postRepository: PostRepository,
    postId: Long,
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
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val updatedPost = postRepository.savePost(state.value.post)
                state.update {
                    it.copy(
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

}
