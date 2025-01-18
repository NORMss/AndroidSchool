package com.eltex.androidschool.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserAuthentication(
    @SerialName("id")
    val id: Long,
    @SerialName("token")
    val token: String,
    @SerialName("avatar")
    val avatar: String?,
)
