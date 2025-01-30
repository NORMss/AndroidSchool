package com.eltex.androidschool.di

import com.eltex.androidschool.domain.mapper.Mapper
import com.eltex.androidschool.domain.model.Event
import com.eltex.androidschool.domain.model.Post
import com.eltex.androidschool.view.fragment.event.EventState
import com.eltex.androidschool.view.fragment.event.adapter.paging.EventPagingModel
import com.eltex.androidschool.view.fragment.post.PostState
import com.eltex.androidschool.view.fragment.post.adapter.paging.PostPagingModel
import com.eltex.androidschool.view.mapper.EventPagingMapper
import com.eltex.androidschool.view.mapper.EventUiMapper
import com.eltex.androidschool.view.mapper.PostPagingMapper
import com.eltex.androidschool.view.mapper.PostUiMapper
import com.eltex.androidschool.view.model.EventUi
import com.eltex.androidschool.view.model.PostUi
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface MapperModule {
    @Binds
    @Singleton
    fun bindEventUiMapper(impl: EventUiMapper): Mapper<Event, EventUi>

    @Binds
    @Singleton
    fun bindPostUiMapper(impl: PostUiMapper): Mapper<Post, PostUi>

    @Binds
    @Singleton
    fun bindEventPagingUiMapper(impl: EventPagingMapper): Mapper<EventState, List<EventPagingModel>>

    @Binds
    @Singleton
    fun bindPostPagingUiMapper(impl: PostPagingMapper): Mapper<PostState, List<PostPagingModel>>
}