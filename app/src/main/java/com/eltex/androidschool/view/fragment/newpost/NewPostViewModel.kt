package com.eltex.androidschool.view.fragment.newpost

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.eltex.androidschool.domain.model.Attachment
import com.eltex.androidschool.domain.model.AttachmentType
import com.eltex.androidschool.domain.model.Coordinates
import com.eltex.androidschool.domain.model.Post
import com.eltex.androidschool.domain.repository.PostRepository
import com.eltex.androidschool.utils.remote.Callback
import com.eltex.androidschool.view.common.Status
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.Instant

class NewPostViewModel(
    private val postRepository: PostRepository,
) : ViewModel() {
    val state: StateFlow<NewPostState>
        field = MutableStateFlow(NewPostState())

    fun setAttachment(uri: Uri) {
        state.update {
            it.copy(
                attachment = Attachment(
                    url = uri.toString(),
                    type = AttachmentType.IMAGE,
                ),
            )
        }
    }

    fun setText(text: String) {
        state.update {
            it.copy(
                textContent = text,
            )
        }
    }

    fun addPost() {
        val textContent = state.value.textContent.trim()
        if (textContent.isEmpty() && state.value.attachment == null) {
            return
        }
        postRepository.savePost(
            post = Post(
                id = 0,
                authorId = 0,
                author = "",
                authorJob = "",
                authorAvatar = "",
                content = state.value.textContent,
                published = Instant.fromEpochSeconds(0),
                coords = Coordinates(
                    lat = 54.9833,
                    long = 82.8964,
                ),
                link = null,
                mentionIds = emptySet(),
                mentionedMe = false,
                likeOwnerIds = emptySet(),
                likedByMe = false,
                attachment = null,
                users = emptyMap(),
            ),
            callback = object : Callback<Post> {
                override fun onSuccess(data: Post) {
                    state.update {
                        it.copy(
                            textContent = data.content,
                            attachment = data.attachment,
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

    fun consumerError() {
        state.update {
            it.copy(
                status = Status.Idle,
            )
        }
    }
}
