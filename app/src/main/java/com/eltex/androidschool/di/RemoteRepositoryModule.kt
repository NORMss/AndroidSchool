package com.eltex.androidschool.di

import com.eltex.androidschool.data.repository.RemoteEventRepository
import com.eltex.androidschool.data.repository.RemotePostRepository
import com.eltex.androidschool.domain.repository.EventRepository
import com.eltex.androidschool.domain.repository.PostRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface RemoteRepositoryModule {
    @Binds
    fun bindRemotePostRepository(impl: RemotePostRepository): PostRepository

    @Binds
    fun bindRemoteEventRepository(impl: RemoteEventRepository): EventRepository
}