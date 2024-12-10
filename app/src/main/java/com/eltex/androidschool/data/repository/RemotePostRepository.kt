package com.eltex.androidschool.data.repository

import com.eltex.androidschool.domain.model.Attachment
import com.eltex.androidschool.domain.model.Coordinates
import com.eltex.androidschool.domain.model.Post
import com.eltex.androidschool.domain.repository.PostRepository
import com.eltex.androidschool.utils.remote.Callback
import kotlinx.datetime.Clock
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.internal.EMPTY_REQUEST
import java.io.IOException

class RemotePostRepository(
    private val client: OkHttpClient
) : PostRepository {
    private companion object {
        val JSON_TYPE = "application/json".toMediaType()
    }

    private val json = Json {
        ignoreUnknownKeys = true
//        coerceInputValues = true
    }

    override fun getPosts(callback: Callback<List<Post>>) {
        val request = Request.Builder()
            .url("https://eltex-android.ru/api/posts")
            .build()
        val call = client.newCall(request)
        call.enqueue(
            object : okhttp3.Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val body = response.body
                        if (body == null) {
                            callback.onError(RuntimeException("Response body is null"))
                            return
                        }
                        try {
                            callback.onSuccess(json.decodeFromString(body.string()))
                        } catch (e: Exception) {
                            callback.onError(e)
                        }
                    } else {
                        callback.onError(RuntimeException("Response code: ${response.code}"))
                    }
                }
            }
        )
    }

    override fun likeById(id: Long, callback: Callback<Post>) {
        val request = Request.Builder()
            .post(EMPTY_REQUEST)
            .url("https://eltex-android.ru/api/posts/$id/likes")
            .build()
        val call = client.newCall(request)
        call.enqueue(
            object : okhttp3.Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val body = response.body
                        if (body == null) {
                            callback.onError(RuntimeException("Response body is null"))
                            return
                        }
                        try {
                            callback.onSuccess(json.decodeFromString(body.string()))
                        } catch (e: Exception) {
                            callback.onError(e)
                        }
                    } else {
                        callback.onError(RuntimeException("Response code: ${response.code}"))
                    }
                }
            }
        )
    }

    override fun savePost(
        id: Long,
        content: String,
        attachment: Attachment?,
        callback: Callback<Post>,
    ) {
        val request = Request.Builder()
            .post(
                json.encodeToString(
                    Post(
                        id = id,
                        authorId = 0,
                        author = "Sergey Bezborodov",
                        authorJob = "Junior Android Developer",
                        authorAvatar = "https://avatars.githubusercontent.com/u/47896309?v=4",
                        content = content,
                        published = Clock.System.now(),
                        coords = Coordinates(
                            lat = 54.9833,
                            long = 82.8964,
                        ),
                        link = "https://github.com/NORMss/",
                        mentionedMe = false,
                        likedByMe = false,
                        attachment = attachment
                    )
                ).toRequestBody(JSON_TYPE)
            )
            .url("https://eltex-android.ru/api/posts")
            .build()
        val call = client.newCall(request)
        call.enqueue(
            object : okhttp3.Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val body = response.body
                        if (body == null) {
                            callback.onError(RuntimeException("Response body is null"))
                            return
                        }
                        try {
                            callback.onSuccess(json.decodeFromString(body.string()))
                        } catch (e: Exception) {
                            callback.onError(e)
                        }
                    } else {
                        callback.onError(RuntimeException("Response code: ${response.code}"))
                    }
                }
            }
        )
    }

    override fun deleteById(id: Long, callback: Callback<Unit>) {
        val request = Request.Builder()
            .delete()
            .url("https://eltex-android.ru/api/posts/$id")
            .build()
        val call = client.newCall(request)
        call.enqueue(
            object : okhttp3.Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        try {
                            callback.onSuccess(Unit)
                        } catch (e: Exception) {
                            callback.onError(e)
                        }
                    } else {
                        callback.onError(RuntimeException("Response code: ${response.code}"))
                    }
                }
            }
        )
    }
}