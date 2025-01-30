package com.eltex.androidschool.di

import com.eltex.androidschool.data.repository.RemoteEventRepository
import com.eltex.androidschool.data.repository.RemotePostRepository
import com.eltex.androidschool.domain.repository.EventRepository
import com.eltex.androidschool.domain.repository.PostRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface RemoteRepositoryModule {
    @Binds
    @Singleton
    fun bindRemotePostRepository(impl: RemotePostRepository): PostRepository

    @Binds
    @Singleton
    fun bindRemoteEventRepository(impl: RemoteEventRepository): EventRepository
}