package com.eltex.androidschool.domain.repository

import com.eltex.androidschool.domain.model.Event
import io.reactivex.rxjava3.core.Single

interface EventRepository {
    fun getEvents(): Single<List<Event>>
    fun likeById(id: Long, isLiked: Boolean): Single<Event>
    fun participateById(id: Long, isParticipated: Boolean): Single<Event>
    fun saveEvent(event: Event): Single<Event>
    fun deleteById(id: Long): Single<Unit>
}