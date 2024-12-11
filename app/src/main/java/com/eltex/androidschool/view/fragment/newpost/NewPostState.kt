package com.eltex.androidschool.view.fragment.newpost

import com.eltex.androidschool.domain.model.Attachment
import com.eltex.androidschool.view.common.Status

data class NewPostState(
    val textContent: String = "",
    val attachment: Attachment? = null,
    val status: Status = Status.Idle,
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