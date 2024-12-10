package com.eltex.androidschool.view.fragment.post

import androidx.lifecycle.ViewModel
import com.eltex.androidschool.domain.model.Post
import com.eltex.androidschool.domain.repository.PostRepository
import com.eltex.androidschool.utils.datetime.DateSeparators
import com.eltex.androidschool.utils.remote.Callback
import com.eltex.androidschool.view.common.Status
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class PostViewModel(
    private val postRepository: PostRepository,
) : ViewModel() {
    val state: StateFlow<PostState>
        field = MutableStateFlow(PostState())

    init {
        loadPosts()
    }

    fun likeById(id: Long) {
        postRepository.likeById(
            id,
            object : Callback<Post> {
                override fun onSuccess(data: Post) {
                    state.update {
                        it.copy(
                            posts = state.value.posts.map {
                                if (it.id == data.id) {
                                    data
                                } else {
                                    it
                                }
                            },
                            status = Status.Idle,
                        )
                    }
                    createPostsByDate(state.value.posts)
                }

                override fun onError(throwable: Throwable) {
                    state.update {
                        it.copy(
                            status = Status.Error(throwable),
                        )
                    }
                }
            }
        )
    }

    fun deletePost(id: Long) {
        postRepository.deleteById(
            id,
            object : Callback<Unit> {
                override fun onSuccess(data: Unit) {
                    state.update {
                        it.copy(
                            posts = state.value.posts.filter { it.id != id },
                            status = Status.Idle,
                        )
                    }
                    createPostsByDate(state.value.posts)
                }

                override fun onError(throwable: Throwable) {
                    state.update {
                        it.copy(
                            status = Status.Error(throwable),
                        )
                    }
                }
            }
        )
    }

    fun loadPosts() {
        postRepository.getPosts(
            object : Callback<List<Post>> {
                override fun onSuccess(data: List<Post>) {
                    state.update {
                        it.copy(
                            posts = data,
                            status = Status.Idle,
                        )
                    }
                    createPostsByDate(state.value.posts)
                }

                override fun onError(throwable: Throwable) {
                    state.update {
                        it.copy(
                            status = Status.Error(throwable),
                        )
                    }
                }
            }
        )
    }

    fun consumerError() {
        state.update {
            it.copy(
                status = Status.Idle,
            )
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
}