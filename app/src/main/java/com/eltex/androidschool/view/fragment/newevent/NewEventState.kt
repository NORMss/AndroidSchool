package com.eltex.androidschool.view.fragment.newevent

import com.eltex.androidschool.domain.model.Attachment

data class NewEventState(
    val textContent: String = "",
    val link: String? = null,
    val attachment: Attachment? = null,
)