package com.eltex.androidschool.view.fragment.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eltex.androidschool.domain.mapper.GroupByDateMapper
import com.eltex.androidschool.domain.model.Post
import com.eltex.androidschool.domain.repository.PostRepository
import com.eltex.androidschool.view.common.Status
import com.eltex.androidschool.view.model.PostUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PostViewModel(
    private val postRepository: PostRepository,
    private val mapper: GroupByDateMapper<Post, PostUi>,
) : ViewModel() {

    val state: StateFlow<PostState>
        field = MutableStateFlow(PostState())

    init {
        loadPosts()
    }

    fun likeById(id: Long, isLiked: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val updatedPost = postRepository.likeById(id = id, isLiked = isLiked)
                val updatedPosts = withContext(Dispatchers.IO) {
                    state.value.posts.map { post ->
                        if (post.id == updatedPost.id) updatedPost else post
                    }
                }
                val postsUi = withContext(Dispatchers.IO) {
                    mapper.map(updatedPosts)
                }
                state.update {
                    it.copy(
                        posts = updatedPosts,
                        postsByDate = postsUi,
                        status = Status.Idle,
                    )
                }
            } catch (e: Exception) {
                state.update {
                    it.copy(
                        status = Status.Error(e),
                    )
                }
            }
        }
    }

    fun deletePost(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                postRepository.deleteById(id)
                val updatedPosts = withContext(Dispatchers.Default) {
                    state.value.posts.filter { it.id != id }
                }
                val postsUi = withContext(Dispatchers.IO) {
                    mapper.map(updatedPosts)
                }
                state.update {
                    it.copy(
                        posts = updatedPosts,
                        postsByDate = postsUi,
                        status = Status.Idle,
                    )
                }
            } catch (e: Exception) {
                state.update {
                    it.copy(
                        status = Status.Error(e),
                    )
                }
            }
        }
    }

    fun loadPosts() {
        state.update { it.copy(status = Status.Loading) }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val loadedPosts = postRepository.getPosts()
                val postsUi = withContext(Dispatchers.IO) {
                    mapper.map(loadedPosts)
                }
                state.update {
                    it.copy(
                        posts = loadedPosts,
                        postsByDate = postsUi,
                        status = Status.Idle,
                    )
                }
            } catch (e: Exception) {
                state.update {
                    it.copy(
                        status = Status.Error(e),
                    )
                }
            }
        }
    }

    fun consumerError() {
        state.update {
            it.copy(
                status = Status.Idle,
            )
        }
    }

}