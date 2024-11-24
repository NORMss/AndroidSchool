package com.eltex.androidschool.view.fragment.post

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eltex.androidschool.domain.model.Post
import com.eltex.androidschool.domain.repository.PostRepository
import com.eltex.androidschool.utils.datetime.DateSeparators
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PostViewModel(
    private val postRepository: PostRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(PostState())
    val state = _state.asStateFlow()

    init {
        observePosts()
    }

    fun likeById(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            postRepository.likeById(id)
        }
    }

    fun deletePost(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            postRepository.deletePostById(id)
        }
    }

    fun editPost(id: Long, textContent: String) {
        viewModelScope.launch(Dispatchers.IO) {
            postRepository.editPostById(id, textContent)
        }
    }

    private fun createPostsByDate(updatedPosts: List<Post>) {
        _state.update { state ->
            val groupedPosts = DateSeparators.groupByDate(
                items = updatedPosts,
            )
            state.copy(posts = updatedPosts, postsByDate = groupedPosts)
        }
    }

    private fun observePosts() {
        postRepository.getPosts()
            .onEach { posts ->
                _state.update { state ->
                    state.copy(posts = posts)
                }
                Log.d("MyLog", _state.value.posts.last().toString())
                createPostsByDate(posts)
            }
            .launchIn(viewModelScope)
    }
}