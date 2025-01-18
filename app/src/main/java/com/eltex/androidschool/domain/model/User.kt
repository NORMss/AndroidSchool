package com.eltex.androidschool.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("id")
    val id: Long,
    @SerialName("login")
    val login: String,
    @SerialName("name")
    val name: String,
    @SerialName("avatar")
    val avatar: String?,
)
