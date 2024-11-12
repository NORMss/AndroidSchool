package com.eltex.androidschool.view.post

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eltex.androidschool.R
import com.eltex.androidschool.domain.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class PostViewModel(
    private val postRepository: PostRepository,
) : ViewModel() {
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

    fun more() {
        sendToast(R.string.not_implemented, true)
        resetToast()
    }

    fun share() {
        sendToast(R.string.not_implemented, true)
        resetToast()
    }

    private fun resetToast() {
        _state.update {
            it.copy(toast = null)
        }
    }

    private fun sendToast(@StringRes res: Int, short: Boolean = true) {
        _state.update {
            it.copy(
                toast = Pair(res, short)
            )
        }
    }
}