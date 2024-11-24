package com.eltex.androidschool.view.activity.post

import com.eltex.androidschool.domain.model.Attachment

data class NewPostState(
    val textContent: String = "",
    val attachment: Attachment? = null,
)