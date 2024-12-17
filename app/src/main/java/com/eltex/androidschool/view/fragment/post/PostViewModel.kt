package com.eltex.androidschool.view.fragment.post

import androidx.lifecycle.ViewModel
import com.eltex.androidschool.domain.model.Post
import com.eltex.androidschool.domain.repository.PostRepository
import com.eltex.androidschool.utils.datetime.DateSeparators
import com.eltex.androidschool.view.common.Status
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class PostViewModel(
    private val postRepository: PostRepository,
) : ViewModel() {
    val disposable = CompositeDisposable()

    val state: StateFlow<PostState>
        field = MutableStateFlow(PostState())

    init {
        loadPosts()
    }

    fun likeById(id: Long, isLiked: Boolean) {
        postRepository.likeById(id = id, isLiked = isLiked)
            .subscribeBy(
                onSuccess = { posts ->
                    state.update {
                        it.copy(
                            posts = state.value.posts.map {
                                if (it.id == posts.id) {
                                    posts
                                } else {
                                    it
                                }
                            },
                            status = Status.Idle,
                        )
                    }
                    createPostsByDate(state.value.posts)

                },
                onError = { throwable ->
                    state.update {
                        it.copy(
                            status = Status.Error(throwable),
                        )
                    }

                }
            ).addTo(disposable)
    }

    fun deletePost(id: Long) {
        postRepository.deleteById(id).subscribeBy(
            onComplete = {
                state.update {
                    it.copy(
                        posts = state.value.posts.filter { it.id != id },
                        status = Status.Idle,
                    )
                }
                createPostsByDate(state.value.posts)
            },
            onError = { throwable ->
                state.update {
                    it.copy(
                        status = Status.Error(throwable),
                    )
                }
            }
        ).addTo(disposable)
    }

    fun loadPosts() {
        state.update { it.copy(status = Status.Loading) }
        postRepository.getPosts().subscribeBy(
            onSuccess = { posts ->
                state.update {
                    it.copy(
                        posts = posts,
                        status = Status.Idle,
                    )
                }
                createPostsByDate(state.value.posts)
            },
            onError = { throwable ->
                state.update {
                    it.copy(
                        status = Status.Error(throwable),
                    )
                }
            }
        ).addTo(disposable)
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

    override fun onCleared() {
        disposable.dispose()
    }
}