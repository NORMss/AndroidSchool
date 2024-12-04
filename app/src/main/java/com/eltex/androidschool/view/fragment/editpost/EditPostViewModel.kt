package com.eltex.androidschool.view.fragment.editpost

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eltex.androidschool.domain.repository.PostRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditPostViewModel(
    private val postRepository: PostRepository,
    private val postId: Long,
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
            postRepository.editPostById(postId, state.value.post.content)
        }
    }

    private fun getPost(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            state.update {
                it.copy(
                    post = postRepository.getPostById(id)
                )
            }
        }
    }
}
