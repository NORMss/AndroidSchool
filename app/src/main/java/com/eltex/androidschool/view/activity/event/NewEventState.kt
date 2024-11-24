package com.eltex.androidschool.view.activity.event

import com.eltex.androidschool.domain.model.Attachment

data class NewEventState(
    val textContent: String = "",
    val link: String? = null,
    val attachment: Attachment? = null,
)