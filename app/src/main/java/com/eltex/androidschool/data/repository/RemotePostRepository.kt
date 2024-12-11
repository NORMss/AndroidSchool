package com.eltex.androidschool.data.repository

import com.eltex.androidschool.data.remote.api.PostApi
import com.eltex.androidschool.domain.model.Post
import com.eltex.androidschool.domain.repository.PostRepository
import com.eltex.androidschool.utils.remote.Callback as DomainCallback
import retrofit2.Callback as RetrofitCallback

class RemotePostRepository(
    private val postApi: PostApi
) : PostRepository {
    override fun getPosts(callback: DomainCallback<List<Post>>) {
        val call = postApi.getPosts()
        call.enqueue(
            object : RetrofitCallback<List<Post>> {
                override fun onResponse(
                    call: retrofit2.Call<List<Post>?>,
                    response: retrofit2.Response<List<Post>?>
                ) {
                    if (response.isSuccessful) {
                        callback.onSuccess(requireNotNull(response.body()))
                    } else {
                        callback.onError(RuntimeException("Response code is ${response.code()}"))
                    }
                }

                override fun onFailure(
                    call: retrofit2.Call<List<Post>?>,
                    e: Throwable
                ) {
                    callback.onError(e)
                }
            }
        )
    }

    override fun likeById(id: Long, isLiked: Boolean, callback: DomainCallback<Post>) {
        val call = when (isLiked) {
            true -> postApi.unlikeById(id)
            false -> postApi.likeById(id)
        }
        call.enqueue(
            object : RetrofitCallback<Post> {
                override fun onResponse(
                    call: retrofit2.Call<Post?>,
                    response: retrofit2.Response<Post?>
                ) {
                    if (response.isSuccessful) {
                        callback.onSuccess(requireNotNull(response.body()))
                    } else {
                        callback.onError(RuntimeException("Response code is ${response.code()}"))
                    }
                }

                override fun onFailure(
                    call: retrofit2.Call<Post?>,
                    e: Throwable
                ) {
                    callback.onError(e)
                }
            }
        )
    }

    override fun savePost(
        post: Post,
        callback: DomainCallback<Post>,
    ) {
        val call = postApi.save(post)
        call.enqueue(
            object : RetrofitCallback<Post> {
                override fun onResponse(
                    call: retrofit2.Call<Post?>,
                    response: retrofit2.Response<Post?>
                ) {
                    if (response.isSuccessful) {
                        callback.onSuccess(requireNotNull(response.body()))
                    } else {
                        callback.onError(RuntimeException("Response code is ${response.code()}"))
                    }
                }

                override fun onFailure(
                    call: retrofit2.Call<Post?>,
                    e: Throwable
                ) {
                    callback.onError(e)
                }
            }
        )
    }

    override fun deleteById(id: Long, callback: DomainCallback<Unit>) {
        val call = postApi.deleteById(id)
        call.enqueue(
            object : RetrofitCallback<Unit> {
                override fun onResponse(
                    call: retrofit2.Call<Unit?>,
                    response: retrofit2.Response<Unit?>
                ) {
                    if (response.isSuccessful) {
                        callback.onSuccess(requireNotNull(response.body()))
                    } else {
                        callback.onError(RuntimeException("Response code is ${response.code()}"))
                    }
                }

                override fun onFailure(call: retrofit2.Call<Unit?>, e: Throwable) {
                    callback.onError(e)
                }
            }
        )
    }
}