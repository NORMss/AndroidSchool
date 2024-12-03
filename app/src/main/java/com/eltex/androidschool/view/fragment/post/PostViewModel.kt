package com.eltex.androidschool.view.fragment.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eltex.androidschool.domain.model.Post
import com.eltex.androidschool.domain.repository.PostRepository
import com.eltex.androidschool.utils.datetime.DateSeparators
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PostViewModel(
    private val postRepository: PostRepository,
) : ViewModel() {
    val state: StateFlow<PostState>
        field = MutableStateFlow(PostState())

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

    private fun createPostsByDate(updatedPosts: List<Post>) {
        state.update { state ->
            val groupedPosts = DateSeparators.groupByDate(
                items = updatedPosts,
            )
            state.copy(posts = updatedPosts, postsByDate = groupedPosts)
        }
    }

    private fun observePosts() {
        postRepository.getPosts()
            .onEach { posts ->
                state.update { state ->
                    state.copy(posts = posts)
                }
                createPostsByDate(posts)
            }
            .launchIn(viewModelScope)
    }
}