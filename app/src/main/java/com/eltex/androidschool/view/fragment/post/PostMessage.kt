package com.eltex.androidschool.view.fragment.post

import arrow.core.Either
import com.eltex.androidschool.domain.model.Post
import com.eltex.androidschool.view.model.PostUi
import com.eltex.androidschool.view.model.PostWithError

sealed interface PostMessage {
    data object LoadNextPage : PostMessage
    data object Refresh : PostMessage
    data class Like(val post: PostUi) : PostMessage
    data class Delete(val post: PostUi) : PostMessage
    data object HandleError : PostMessage

    data class DeleteError(val error: PostWithError) : PostMessage
    data class LikeResult(val result: Either<PostWithError, Post>) : PostMessage
    data class InitialLoaded(val result: Either<Throwable, List<Post>>) : PostMessage
    data class NextPageLoaded(val result: Either<Exception, List<Post>>) : PostMessage

}