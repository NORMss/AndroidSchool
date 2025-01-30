package com.eltex.androidschool.view.fragment.post.reducer

import arrow.core.Either
import com.eltex.androidschool.domain.mapper.Mapper
import com.eltex.androidschool.domain.model.Event
import com.eltex.androidschool.domain.model.Post
import com.eltex.androidschool.mvi.Reducer
import com.eltex.androidschool.mvi.ReducerResult
import com.eltex.androidschool.view.fragment.post.PostEffect
import com.eltex.androidschool.view.fragment.post.PostMessage
import com.eltex.androidschool.view.fragment.post.PostState
import com.eltex.androidschool.view.fragment.post.PostStatus
import com.eltex.androidschool.view.model.PostUi
import javax.inject.Inject

/**
 * `PostReducer` is a crucial component in managing the state of events within the application.
 * It acts as a pure function that takes the current `EventState` and an incoming `EventMessage`,
 * and then produces a new [PostState] along with any associated side-effects [PostEffect].
 *
 * The [PostReducer] is responsible for handling various actions related to events, such as:
 * - Deleting events
 * - Managing user participation in events
 * - Handling errors during event deletion or participation
 * - Loading the initial list of events
 * - Handling user likes on events
 * - Loading the next page of events (pagination)
 * - Refreshing the list of events
 * - Handle errors in the state.
 *
 * It utilizes a [Mapper] to transform data from the domain layer ([Event]) to the UI layer ([PostUi]).
 *
 * @property mapper A [Mapper] instance used to transform [Post] objects to [PostUi] objects.
 *   This is essential for adapting data from the domain layer to the presentation layer.
 */
class PostReducer @Inject constructor(
    private val mapper: Mapper<Post, PostUi>,
) : Reducer<PostState, PostMessage, PostEffect> {
    override fun reduce(
        old: PostState,
        message: PostMessage
    ): ReducerResult<PostState, PostEffect> {
        return when (message) {
            is PostMessage.Delete -> ReducerResult(
                old.posts.filter { it.id != message.post.id }.let { updatedPosts ->
                    old.copy(
                        posts = updatedPosts,
                    )
                },
                PostEffect.Delete(message.post),
            )

            is PostMessage.DeleteError -> ReducerResult(
                buildList(old.posts.size + 1) {
                    val post = message.error.post
                    addAll(old.posts.filter { it.id > post.id })
                    add(post)
                    addAll(old.posts.filter { it.id < post.id })
                }.let { updatedPosts ->
                    old.copy(
                        posts = updatedPosts,
                    )
                }
            )

            PostMessage.HandleError -> ReducerResult(
                old.copy(singleError = null)
            )

            is PostMessage.InitialLoaded -> ReducerResult(
                when (val messageResult = message.result) {
                    is Either.Left -> {
                        if (old.posts.isNotEmpty()) {
                            old.copy(
                                singleError = messageResult.value,
                                status = PostStatus.Idle(),
                            )
                        } else {
                            old.copy(status = PostStatus.EmptyError(messageResult.value))
                        }
                    }

                    is Either.Right -> {
                        messageResult.value.map(mapper::map).let { updatedPosts ->
                            old.copy(
                                posts = updatedPosts,
                                status = PostStatus.Idle(),
                            )
                        }
                    }
                }
            )

            is PostMessage.Like -> {
                val updatedPosts = old.posts.map { post ->
                    if (post.id == message.post.id) {
                        post.copy(
                            likedByMe = !post.likedByMe,
                            likes = if (post.likedByMe) post.likes - 1 else post.likes + 1
                        )
                    } else {
                        post
                    }
                }
                ReducerResult(
                    old.copy(
                        posts = updatedPosts,
                    ),
                    PostEffect.Like(message.post)
                )
            }

            is PostMessage.LikeResult -> ReducerResult(
                when (val messageResult = message.result) {
                    is Either.Left -> {
                        val resultValue = messageResult.value
                        val post = resultValue.post
                        val updatedPosts = old.posts.map {
                            if (it.id == post.id) {
                                post
                            } else {
                                it
                            }
                        }
                        old.copy(
                            posts = updatedPosts,
                            singleError = resultValue.error,
                        )
                    }

                    is Either.Right -> {
                        val post = mapper.map(messageResult.value)
                        val updatedPosts = old.posts.map {
                            if (it.id == post.id) {
                                post
                            } else {
                                it
                            }
                        }
                        old.copy(
                            posts = updatedPosts,
                        )
                    }
                }
            )

            is PostMessage.NextPageLoaded -> ReducerResult(
                when (val messageResult = message.result) {
                    is Either.Left -> {
                        old.copy(
                            status = PostStatus.NextPageError(messageResult.value),
                        )
                    }

                    is Either.Right -> {
                        val updatedPosts = old.posts + messageResult.value.map(mapper::map)
                        val isLoadingFinished = message.result.value.size < PAGE_SIZE
                        old.copy(
                            posts = updatedPosts,
                            status = PostStatus.Idle(isLoadingFinished),
                        )
                    }
                }
            )

            PostMessage.LoadNextPage -> {
                val isLoadingFinished = (old.status as? PostStatus.Idle)?.isLoadingFinished == true
                val effect = if (isLoadingFinished) {
                    null
                } else
                    PostEffect.LoadNextPage(old.posts.last().id, PAGE_SIZE)

                val status = if (isLoadingFinished) old.status else PostStatus.NextPageLoading
                ReducerResult(
                    old.copy(status = status),
                    effect,
                )
            }

            PostMessage.Refresh -> ReducerResult(

                old.copy(
                    status = if (old.posts.isNotEmpty()) {
                        PostStatus.Refreshing
                    } else {
                        PostStatus.EmptyLoading
                    }
                ),
                PostEffect.LoadInitialPage(PAGE_SIZE),
            )
        }
    }

    companion object {
        const val PAGE_SIZE = 10
    }
}