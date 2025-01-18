package com.eltex.androidschool.domain.repository

import com.eltex.androidschool.domain.model.User
import com.eltex.androidschool.domain.model.UserAuthentication

interface UserRepository {
    suspend fun registrationUser(
        login: String,
        pass: String,
        name: String,
    )

    suspend fun authenticationUser(
        login: String,
        pass: String,
    ): UserAuthentication

    suspend fun getUsers(): List<User>

    suspend fun getUser(id: Long): User
}