package com.eltex.androidschool.view.post

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eltex.androidschool.domain.repository.PostRepository
import com.eltex.androidschool.utils.datatime.DateSeparators
import com.eltex.androidschool.utils.resourcemanager.ResourceManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PostViewModel(
    private val postRepository: PostRepository,
    private val resourceManager: ResourceManager,
) : ViewModel() {
    private val _state = MutableStateFlow(PostState())
    val state = _state.asStateFlow()

    init {
        postRepository.getPosts().onEach {
            _state.update { state ->
                state.copy(posts = it)
            }
            createPostsByDate()
        }.launchIn(viewModelScope)
    }

    fun likeById(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            postRepository.likeById(id)
            refreshPosts()
        }
    }

    fun addPost(textContent: String, imageContent: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            postRepository.addPost(textContent, imageContent)
            refreshPosts()
        }
    }

    fun deletePost(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            postRepository.deletePostById(id)
            refreshPosts()
        }
    }

    fun editPost(id: Long, textContent: String) {
        viewModelScope.launch(Dispatchers.IO) {
            postRepository.editPostById(id, textContent)
            refreshPosts()
        }
    }

    private suspend fun refreshPosts() {
        val posts = postRepository.getPosts().first()
        _state.update { state ->
            state.copy(posts = posts)
        }
        createPostsByDate()
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

    override fun onCleared() {
        super.onCleared()
        Log.d("MyLog", "onCleared called")
    }
}