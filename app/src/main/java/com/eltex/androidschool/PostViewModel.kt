package com.eltex.androidschool

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eltex.androidschool.domain.model.Post
import com.eltex.androidschool.domain.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PostViewModel(private val postRepository: PostRepository) : ViewModel() {
    private val _state = MutableStateFlow(PostState())
    val state = _state.asStateFlow()

    init {
        postRepository.getPost().onEach { post ->
            _state.update { state ->
                state.copy(post = post)
            }
        }.launchIn(viewModelScope)
    }

    fun like() {
        postRepository.like()
    }

    fun getPost() {
        viewModelScope.launch {
            postRepository.getPost().onEach { post ->
                _state.update { state ->
                    state.copy(
                        post = post
                    )
                }
            }
        }
    }
}