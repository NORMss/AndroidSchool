package com.eltex.androidschool.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UserPreview(
    val avatar: String?,
    val name: String,
)