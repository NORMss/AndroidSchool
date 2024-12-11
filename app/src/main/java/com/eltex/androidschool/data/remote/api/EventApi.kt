package com.eltex.androidschool.data.remote.api

import com.eltex.androidschool.data.remote.RetrofitFactory
import com.eltex.androidschool.domain.model.Event
import retrofit2.Call
import retrofit2.create
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface EventApi {
    @GET("api/events")
    fun getEvents(): Call<List<Event>>

    @POST("api/events")
    fun save(@Body event: Event): Call<Event>

    @DELETE("api/events/{id}")
    fun deleteById(@Path("id") id: Long): Call<Unit>

    @POST("api/events/{id}/likes")
    fun likeById(@Path("id") id: Long): Call<Event>

    @DELETE("api/events/{id}/likes")
    fun unlikeById(@Path("id") id: Long): Call<Event>

    @POST("api/events/{id}/participants")
    fun participantById(@Path("id") id: Long): Call<Event>

    @DELETE("api/events/{id}/participants")
    fun unparticipantById(@Path("id") id: Long): Call<Event>

    companion object {
        val INSTANCE: EventApi by lazy {
            RetrofitFactory.INSTANCE.create()
        }
    }

}