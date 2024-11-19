package com.eltex.androidschool.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Attachment(
    val url: String,
    val type: AttachmentType,
)