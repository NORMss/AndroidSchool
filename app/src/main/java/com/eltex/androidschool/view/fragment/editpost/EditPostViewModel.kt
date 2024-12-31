package com.eltex.androidschool.view.fragment.editpost

import androidx.lifecycle.ViewModel
import com.eltex.androidschool.domain.repository.PostRepository
import com.eltex.androidschool.domain.rx.SchedulersProvider
import com.eltex.androidschool.view.common.Status
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class EditPostViewModel(
    private val postRepository: PostRepository,
    postId: Long,
    private val schedulersProvider: SchedulersProvider = SchedulersProvider.DEFAULT,
) : ViewModel() {
    val disposable = CompositeDisposable()

    val state: StateFlow<EditPostState>
        field = MutableStateFlow(EditPostState())

    init {
        getPost(postId)
    }

    fun setText(text: String) {
        state.update {
            it.copy(
                post = state.value.post.copy(
                    content = text,
                )
            )
        }
    }

    fun editPost() {
        postRepository.savePost(state.value.post)
            .subscribeBy(
                onSuccess = { post ->
                    state.update {
                        it.copy(
                            post = post,
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

    private fun getPost(id: Long) {
        state.update { it.copy(status = Status.Loading) }
        postRepository.getPosts()
            .observeOn(schedulersProvider.io)
            .map { posts ->
                posts.filter { it.id == id }.first()
            }
            .observeOn(schedulersProvider.mainThread)
            .subscribeBy(
                onSuccess = { post ->
                    state.update {
                        it.copy(
                            post = post,
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

    override fun onCleared() {
        disposable.dispose()
    }

}
