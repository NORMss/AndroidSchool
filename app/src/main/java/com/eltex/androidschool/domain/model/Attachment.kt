package com.eltex.androidschool.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Attachment(
    @SerialName("url")
    val url: String,
    @SerialName("type")
    val type: AttachmentType,
)