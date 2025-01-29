package com.eltex.androidschool.view.fragment.newevent

import com.eltex.androidschool.domain.model.Event
import com.eltex.androidschool.view.common.Status
import com.eltex.androidschool.view.model.FileModel
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

data class NewEventState(
    val result: Event? = null,
    val textContent: String = "",
    val link: String? = null,
    val dateTime: Instant = Clock.System.now(),
    val file: FileModel? = null,
    val status: Status = Status.Idle,
) {
    val isRefreshing: Boolean
        get() = status == Status.Loading && textContent.isNotBlank()
    val isEmptyLoading: Boolean
        get() = status == Status.Loading && textContent.isBlank()
    val isEmptyError: Boolean
        get() = status is Status.Error && textContent.isNotBlank()
    val isRefreshError: Boolean
        get() = status is Status.Error && textContent.isNotBlank()
}
