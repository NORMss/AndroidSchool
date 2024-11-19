package com.eltex.androidschool.view.post

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eltex.androidschool.R
import com.eltex.androidschool.domain.repository.PostRepository
import com.eltex.androidschool.utils.datatime.DateSeparators
import com.eltex.androidschool.utils.resourcemanager.ResourceManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class PostViewModel(
    private val postRepository: PostRepository,
    private val resourceManager: ResourceManager,
) : ViewModel() {
    private val _state = MutableStateFlow(PostState())
    val state = _state.asStateFlow()

    init {
        postRepository.getPost().onEach { posts ->
            _state.update { state ->
                state.copy(posts = posts)
            }
            createPostsByDate()
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

    private fun createPostsByDate() {
        if (_state.value.posts.isNotEmpty()) {
            _state.update {
                it.copy(
                    postsByDate = DateSeparators.groupByDate(
                        items = _state.value.posts,
                        resourceManager = resourceManager,
                    )
                )
            }
        }
    }

    fun addPost(textContent: String) {
        postRepository.addPost(textContent)
    }

    fun deletePost(id: Long) {
        postRepository.deletePostById(id)
    }

    fun editPost(id: Long, textContent: String) {
        postRepository.editPostById(id, textContent)
    }
}