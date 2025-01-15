@file:OptIn(ExperimentalCoroutinesApi::class)

package com.eltex.androidschool.view.fragment.post.effecthendler

import android.util.Log
import arrow.core.left
import arrow.core.right
import com.eltex.androidschool.domain.repository.PostRepository
import com.eltex.androidschool.mvi.EffectHandler
import com.eltex.androidschool.view.fragment.post.PostEffect
import com.eltex.androidschool.view.fragment.post.PostMessage
import com.eltex.androidschool.view.mapper.PostUiMapper
import com.eltex.androidschool.view.model.PostWithError
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.merge

class PostEffectHandler(
    private val repository: PostRepository,
    private val mapper: PostUiMapper,
) : EffectHandler<PostEffect, PostMessage> {
    override fun connect(effects: Flow<PostEffect>): Flow<PostMessage> {
        return listOf(
            handleInitialPage(effects),
            handleNextPage(effects),
            handleDelete(effects),
            handleLike(effects),
        )
            .merge()
    }

    private fun handleDelete(effects: Flow<PostEffect>): Flow<PostMessage.DeleteError> =
        effects.filterIsInstance<PostEffect.Delete>()
            .mapLatest {
                try {
                    repository.deleteById(it.post.id)
                } catch (e: Exception) {
                    if (e is CancellationException) throw e
                    PostMessage.DeleteError(PostWithError(it.post, e))
                }
            }
            .filterIsInstance<PostMessage.DeleteError>()

    private fun handleLike(effects: Flow<PostEffect>): Flow<PostMessage.LikeResult> =
        effects.filterIsInstance<PostEffect.Like>()
            .mapLatest {
                PostMessage.LikeResult(
                    try {
                        repository.likeById(it.post.id, it.post.likedByMe)
                            .let(mapper::map)
                            .right()
                    } catch (e: Exception) {
                        if (e is CancellationException) throw e
                        PostWithError(it.post, e)
                            .left()
                    }
                )
            }

    private fun handleNextPage(effects: Flow<PostEffect>): Flow<PostMessage.NextPageLoaded> =
        effects.filterIsInstance<PostEffect.LoadNextPage>()
            .mapLatest {
                PostMessage.NextPageLoaded(
                    try {
                        repository.getPostsBefore(it.id, it.count)
                            .map(mapper::map)
                            .right()
                    } catch (e: Exception) {
                        if (e is CancellationException) throw e
                        e.left()
                    }
                )
            }

    private fun handleInitialPage(effects: Flow<PostEffect>): Flow<PostMessage.InitialLoaded> =
        effects.filterIsInstance<PostEffect.LoadInitialPage>()
            .mapLatest {
                PostMessage.InitialLoaded(
                    try {
                        repository.getPostsLatest(it.count)
                            .map(mapper::map)
                            .right()
                    } catch (e: Exception) {
                        if (e is CancellationException) throw e
                        e.left()
                    }
                )
            }
}