package com.eltex.androidschool.data.repository

import com.eltex.androidschool.data.remote.api.UserApi
import com.eltex.androidschool.domain.model.User
import com.eltex.androidschool.domain.model.UserAuthentication
import com.eltex.androidschool.domain.repository.UserRepository

class RemoteUserRepository(
    private val userApi: UserApi,
) : UserRepository {
    override suspend fun registrationUser(
        login: String,
        pass: String,
        name: String
    ) {
        return userApi.registrationUser(login, pass, name)
    }

    override suspend fun authenticationUser(
        login: String,
        pass: String
    ): UserAuthentication {
        return userApi.authenticationUser(login, pass)
    }

    override suspend fun getUsers(): List<User> {
        return userApi.getUsers()
    }

    override suspend fun getUser(id: Long): User {
        return userApi.getUser(id)
    }
}