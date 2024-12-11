package com.eltex.androidschool.data.repository

import com.eltex.androidschool.data.remote.api.EventApi
import com.eltex.androidschool.domain.model.Event
import com.eltex.androidschool.domain.repository.EventRepository
import com.eltex.androidschool.utils.remote.Callback as DomainCallback
import retrofit2.Callback as RetrofitCallback

class RemoteEventRepository(
    private val eventApi: EventApi,
) : EventRepository {

    override fun getEvents(callback: DomainCallback<List<Event>>) {
        val call = eventApi.getEvents()
        call.enqueue(
            object : RetrofitCallback<List<Event>> {
                override fun onResponse(
                    call: retrofit2.Call<List<Event>?>,
                    response: retrofit2.Response<List<Event>?>
                ) {
                    if (response.isSuccessful) {
                        callback.onSuccess(requireNotNull(response.body()))
                    } else {
                        callback.onError(RuntimeException("Response code is ${response.code()}"))
                    }
                }

                override fun onFailure(
                    call: retrofit2.Call<List<Event>?>,
                    e: Throwable
                ) {
                    callback.onError(e)
                }
            }
        )
    }

    override fun likeById(id: Long, isLiked: Boolean, callback: DomainCallback<Event>) {
        val call = when (isLiked) {
            true -> eventApi.unlikeById(id)
            false -> eventApi.likeById(id)
        }
        call.enqueue(
            object : RetrofitCallback<Event> {
                override fun onResponse(
                    call: retrofit2.Call<Event?>,
                    response: retrofit2.Response<Event?>
                ) {
                    if (response.isSuccessful) {
                        callback.onSuccess(requireNotNull(response.body()))
                    } else {
                        callback.onError(RuntimeException("Response code is ${response.code()}"))
                    }
                }

                override fun onFailure(
                    call: retrofit2.Call<Event?>,
                    e: Throwable
                ) {
                    callback.onError(e)
                }
            }
        )
    }

    override fun participateById(
        id: Long,
        isParticipated: Boolean,
        callback: DomainCallback<Event>
    ) {
        val call = when (isParticipated) {
            true -> eventApi.unparticipantById(id)
            false -> eventApi.participantById(id)
        }
        call.enqueue(
            object : RetrofitCallback<Event> {
                override fun onResponse(
                    call: retrofit2.Call<Event?>,
                    response: retrofit2.Response<Event?>
                ) {
                    if (response.isSuccessful) {
                        callback.onSuccess(requireNotNull(response.body()))
                    } else {
                        callback.onError(RuntimeException("Response code is ${response.code()}"))
                    }
                }

                override fun onFailure(
                    call: retrofit2.Call<Event?>,
                    e: Throwable
                ) {
                    callback.onError(e)
                }
            }
        )
    }


    override fun saveEvent(
        event: Event,
        callback: DomainCallback<Event>,
    ) {
        val call = eventApi.save(event)
        call.enqueue(
            object : RetrofitCallback<Event> {
                override fun onResponse(
                    call: retrofit2.Call<Event?>,
                    response: retrofit2.Response<Event?>
                ) {
                    if (response.isSuccessful) {
                        callback.onSuccess(requireNotNull(response.body()))
                    } else {
                        callback.onError(RuntimeException("Response code is ${response.code()}"))
                    }
                }

                override fun onFailure(
                    call: retrofit2.Call<Event?>,
                    e: Throwable
                ) {
                    callback.onError(e)
                }
            }
        )
    }

    override fun deleteById(id: Long, callback: DomainCallback<Unit>) {
        val call = eventApi.deleteById(id)
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