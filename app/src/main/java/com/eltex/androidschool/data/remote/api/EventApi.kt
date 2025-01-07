package com.eltex.androidschool.data.remote.api

import com.eltex.androidschool.data.remote.RetrofitFactory
import com.eltex.androidschool.domain.model.Event
import io.reactivex.rxjava3.core.Single
import retrofit2.create
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface EventApi {
    @GET("api/events")
    suspend fun getEvents(): List<Event>

    @POST("api/events")
    suspend fun save(@Body event: Event): Event

    @DELETE("api/events/{id}")
    suspend fun deleteById(@Path("id") id: Long)

    @POST("api/events/{id}/likes")
    suspend fun likeById(@Path("id") id: Long): Event

    @DELETE("api/events/{id}/likes")
    suspend fun unlikeById(@Path("id") id: Long): Event

    @POST("api/events/{id}/participants")
    suspend fun participantById(@Path("id") id: Long): Event

    @DELETE("api/events/{id}/participants")
    suspend fun unparticipantById(@Path("id") id: Long): Event

    companion object {
        val INSTANCE: EventApi by lazy {
            RetrofitFactory.INSTANCE.create()
        }
    }

}