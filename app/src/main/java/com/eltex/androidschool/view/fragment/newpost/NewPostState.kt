package com.eltex.androidschool.view.fragment.newpost

import com.eltex.androidschool.domain.model.Post
import com.eltex.androidschool.view.common.Status
import com.eltex.androidschool.view.model.FileModel

data class NewPostState(
    val result: Post? = null,
    val textContent: String = "",
    val status: Status = Status.Idle,
    val file: FileModel? = null,
) {
    val isRefreshing: Boolean
        get() = status == Status.Loading && textContent.isNotBlank()
    val isEmptyLoading: Boolean
        get() = status == Status.Loading && textContent.isBlank()
    val isEmptyError: Boolean
        get() = status is Status.Error && textContent.isBlank()
    val isRefreshError: Boolean
        get() = status is Status.Error && textContent.isNotBlank()
}