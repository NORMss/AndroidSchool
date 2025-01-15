@file:OptIn(ExperimentalCoroutinesApi::class)

package com.eltex.androidschool.view.fragment.post.effecthendler

import arrow.core.left
import arrow.core.right
import com.eltex.androidschool.domain.repository.PostRepository
import com.eltex.androidschool.mvi.EffectHandler
import com.eltex.androidschool.view.fragment.post.PostEffect
import com.eltex.androidschool.view.fragment.post.PostMessage
import com.eltex.androidschool.view.mapper.PostGroupByDateMapper
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.merge

class PostEffectHandler(
    private val repository: PostRepository,
    private val mapper: PostGroupByDateMapper,
) : EffectHandler<PostEffect, PostMessage> {
    override fun connect(effects: Flow<PostEffect>): Flow<PostMessage> {
        return listOf(
            effects.filterIsInstance<PostEffect.LoadNextPage>()
                .mapLatest {
                    PostMessage.NextPageLoaded(
                        try {
                            repository.getPostsBefore(it.id, it.count)
                                .let { mapper.map(it) }.right()
                        } catch (e: Exception) {
                            if (e is CancellationException) throw e
                            e.left()
                        }
                    )
                }
        )
            .merge()
    }
}