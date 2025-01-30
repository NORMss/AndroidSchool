package com.eltex.androidschool.di

import com.eltex.androidschool.mvi.Store
import com.eltex.androidschool.view.fragment.event.EventEffect
import com.eltex.androidschool.view.fragment.event.EventMessage
import com.eltex.androidschool.view.fragment.event.EventState
import com.eltex.androidschool.view.fragment.event.EventStore
import com.eltex.androidschool.view.fragment.event.effecthendler.EventEffectHandler
import com.eltex.androidschool.view.fragment.event.reducer.EventReducer
import com.eltex.androidschool.view.fragment.post.PostEffect
import com.eltex.androidschool.view.fragment.post.PostMessage
import com.eltex.androidschool.view.fragment.post.PostState
import com.eltex.androidschool.view.fragment.post.PostStore
import com.eltex.androidschool.view.fragment.post.effecthendler.PostEffectHandler
import com.eltex.androidschool.view.fragment.post.reducer.PostReducer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
object StoreModule {
    @Provides
    fun providePostStoreModule(
        reducer: PostReducer,
        effectHandler: PostEffectHandler,
    ): Store<PostState, PostMessage, PostEffect> {
        return PostStore(
            reducer = reducer,
            effectHandler = effectHandler,
            initMessages = setOf(PostMessage.Refresh),
            initState = PostState(),
        )
    }

    @Provides
    fun provideEventStoreModule(
        reducer: EventReducer,
        effectHandler: EventEffectHandler,
    ): Store<EventState, EventMessage, EventEffect> {
        return EventStore(
            reducer = reducer,
            effectHandler = effectHandler,
            initMessages = setOf(EventMessage.Refresh),
            initState = EventState(),
        )
    }
}