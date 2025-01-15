package com.eltex.androidschool.view.fragment.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eltex.androidschool.mvi.PostStore
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PostViewModel(
    private val store: PostStore,
//    private val postRepository: PostRepository,
//    private val mapper: GroupByDateMapper<Post, PostUi>,
) : ViewModel() {

    val state: StateFlow<PostState> = store.state

//    val state: StateFlow<PostState>
//        field = MutableStateFlow(PostState())

    init {
        viewModelScope.launch {
            store.connect()
        }
    }

    fun accept(message: PostMessage) {
        store.accept(message)
    }

//    fun likeById(id: Long, isLiked: Boolean) {
//        viewModelScope.launch {
//            try {
//                val updatedPost = postRepository.likeById(id = id, isLiked = isLiked)
//                val updatedPosts = withContext(Dispatchers.Default) {
//                    state.value.posts.map { post ->
//                        if (post.id == updatedPost.id) updatedPost else post
//                    }
//                }
//                val postsUi = withContext(Dispatchers.Default) {
//                    mapper.map(updatedPosts)
//                }
//                state.update {
//                    it.copy(
//                        posts = updatedPosts,
//                        postsByDate = postsUi,
//                        status = Status.Idle,
//                    )
//                }
//            } catch (e: Exception) {
//                state.update {
//                    it.copy(
//                        status = Status.Error(e),
//                    )
//                }
//            }
//        }
//    }
//
//    fun deletePost(id: Long) {
//        viewModelScope.launch {
//            try {
//                postRepository.deleteById(id)
//                val updatedPosts = withContext(Dispatchers.Default) {
//                    state.value.posts.filter { it.id != id }
//                }
//                val postsUi = withContext(Dispatchers.Default) {
//                    mapper.map(updatedPosts)
//                }
//                state.update {
//                    it.copy(
//                        posts = updatedPosts,
//                        postsByDate = postsUi,
//                        status = Status.Idle,
//                    )
//                }
//            } catch (e: Exception) {
//                state.update {
//                    it.copy(
//                        status = Status.Error(e),
//                    )
//                }
//            }
//        }
//    }
//
//    fun loadPosts() {
//        state.update { it.copy(status = Status.Loading) }
//        viewModelScope.launch {
//            try {
//                val loadedPosts = postRepository.getPosts()
//                val postsUi = withContext(Dispatchers.Default) {
//                    mapper.map(loadedPosts)
//                }
//                state.update {
//                    it.copy(
//                        posts = loadedPosts,
//                        postsByDate = postsUi,
//                        status = Status.Idle,
//                    )
//                }
//            } catch (e: Exception) {
//                state.update {
//                    it.copy(
//                        status = Status.Error(e),
//                    )
//                }
//            }
//        }
//    }
//
//    fun consumerError() {
//        state.update {
//            it.copy(
//                status = Status.Idle,
//            )
//        }
//    }

}