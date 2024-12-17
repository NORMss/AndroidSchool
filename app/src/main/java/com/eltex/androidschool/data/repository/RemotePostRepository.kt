package com.eltex.androidschool.data.repository

import com.eltex.androidschool.data.remote.api.PostApi
import com.eltex.androidschool.domain.model.Post
import com.eltex.androidschool.domain.repository.PostRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class RemotePostRepository(
    private val postApi: PostApi
) : PostRepository {
    override fun getPosts(): Single<List<Post>> {
        return postApi.getPosts()
    }

    override fun likeById(
        id: Long,
        isLiked: Boolean
    ): Single<Post> {
        return when (isLiked) {
            true -> postApi.unlikeById(id)
            false -> postApi.likeById(id)
        }
    }

    override fun savePost(post: Post): Single<Post> {
        return postApi.save(post)
    }

    override fun deleteById(id: Long): Completable {
        return postApi.deleteById(id)
    }
}