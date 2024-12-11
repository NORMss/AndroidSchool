package com.eltex.androidschool.view.fragment.editpost

import androidx.lifecycle.ViewModel
import com.eltex.androidschool.domain.model.Post
import com.eltex.androidschool.domain.repository.PostRepository
import com.eltex.androidschool.utils.remote.Callback
import com.eltex.androidschool.view.common.Status
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class EditPostViewModel(
    private val postRepository: PostRepository,
    postId: Long,
) : ViewModel() {
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
        postRepository.savePost(
            state.value.post,
            object : Callback<Post> {
                override fun onSuccess(data: Post) {
                    state.update {
                        it.copy(
                            post = data,
                            status = Status.Idle,
                        )
                    }
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

    private fun getPost(id: Long) {
        state.update { it.copy(status = Status.Loading) }
        postRepository.getPosts(
            object : Callback<List<Post>> {
                override fun onSuccess(data: List<Post>) {
                    state.update {
                        it.copy(
                            post = data.filter { it.id == id }.first(),
                            status = Status.Idle,
                        )
                    }
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
}
