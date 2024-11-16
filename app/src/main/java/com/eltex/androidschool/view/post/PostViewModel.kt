package com.eltex.androidschool.view.post

import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eltex.androidschool.R
import com.eltex.androidschool.domain.repository.PostRepository
import com.eltex.androidschool.utils.datatime.DateSeparators
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
        postRepository.getPost().onEach { posts ->
            _state.update { state ->
                state.copy(posts = posts)
            }
            createMapPostByDate()
        }.launchIn(viewModelScope)
    }

    fun likeById(id: Long) {
        postRepository.likeById(id)
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

    private fun createMapPostByDate() {
        if (_state.value.posts.isNotEmpty()) {
            _state.update {
                it.copy(
                    postsByDate = DateSeparators.groupByDate(_state.value.posts)
                )
            }
            Log.d("MyLog", _state.value.postsByDate.toString())
        }
    }
}