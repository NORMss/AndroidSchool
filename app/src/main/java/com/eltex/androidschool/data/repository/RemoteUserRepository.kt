package com.eltex.androidschool.data.repository

import com.eltex.androidschool.data.remote.api.UserApi
import com.eltex.androidschool.domain.model.User
import com.eltex.androidschool.domain.model.UserAuthentication
import com.eltex.androidschool.domain.repository.UserRepository

/**
 * [RemoteUserRepository] is an implementation of [UserRepository] that fetches user data from a remote source
 * using the provided [UserApi].
 *
 * This class handles interactions with the remote user API to perform operations such as user registration,
 * authentication, and retrieval of user information.
 *
 * @property userApi The [UserApi] instance used to communicate with the remote API.
 */
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