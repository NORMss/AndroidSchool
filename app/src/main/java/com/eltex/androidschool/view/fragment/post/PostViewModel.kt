package com.eltex.androidschool.view.fragment.post

import androidx.lifecycle.ViewModel
import com.eltex.androidschool.domain.mapper.GroupByDateMapper
import com.eltex.androidschool.domain.model.Post
import com.eltex.androidschool.domain.repository.PostRepository
import com.eltex.androidschool.domain.rx.SchedulersProvider
import com.eltex.androidschool.view.common.Status
import com.eltex.androidschool.view.model.PostUi
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class PostViewModel(
    private val postRepository: PostRepository,
    private val mapper: GroupByDateMapper<Post, PostUi>,
    private val schedulersProvider: SchedulersProvider = SchedulersProvider.DEFAULT,
) : ViewModel() {
    private val disposable = CompositeDisposable()

    val state: StateFlow<PostState>
        field = MutableStateFlow(PostState())

    init {
        loadPosts()
    }

    fun likeById(id: Long, isLiked: Boolean) {
        postRepository.likeById(id = id, isLiked = isLiked)
            .observeOn(schedulersProvider.io)
            .map { updatedPost ->
                val updatedPosts = state.value.posts.map { post ->
                    if (post.id == updatedPost.id) updatedPost else post
                }
                Pair(
                    updatedPosts,
                    mapper.map(updatedPosts),
                )
            }
            .observeOn(schedulersProvider.mainThread)
            .subscribeBy(
                onSuccess = { (updatedPosts, postsByDate) ->
                    state.update {
                        it.copy(
                            posts = updatedPosts,
                            postsByDate = postsByDate,
                            status = Status.Idle,
                        )
                    }
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
        postRepository.deleteById(id)
            .observeOn(schedulersProvider.io)
            .map {
                val updatedPosts = state.value.posts.filter { it.id != id }
                Pair(
                    updatedPosts,
                    mapper.map(updatedPosts),
                )
            }
            .subscribeBy(
                onSuccess = { (updatedPosts, postsByDate) ->
                    state.value.posts.filter { it.id != id }.also { posts ->
                        state.update {
                            it.copy(
                                posts = updatedPosts,
                                postsByDate = postsByDate,
                                status = Status.Idle,
                            )
                        }
                    }
                },
                onError =
                { throwable ->
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
        postRepository.getPosts()
            .observeOn(schedulersProvider.io)
            .map { posts ->
                Pair(
                    posts,
                    mapper.map(posts)
                )
            }
            .subscribeBy(
                onSuccess = { (posts, postsByDate) ->
                    state.update {
                        it.copy(
                            posts = posts,
                            postsByDate = postsByDate,
                            status = Status.Idle,
                        )
                    }
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

    override fun onCleared() {
        disposable.dispose()
    }
}