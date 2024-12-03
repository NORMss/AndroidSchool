package com.eltex.androidschool.view.fragment.newpost

import com.eltex.androidschool.domain.model.Attachment

data class NewPostState(
    val textContent: String = "",
    val attachment: Attachment? = null,
)