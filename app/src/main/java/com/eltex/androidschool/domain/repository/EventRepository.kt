package com.eltex.androidschool.domain.repository

import com.eltex.androidschool.domain.model.Event
import io.reactivex.rxjava3.core.Single

interface EventRepository {
    fun getEvents(): Single<List<Event>> = Single.never()
    fun likeById(id: Long, isLiked: Boolean): Single<Event> = Single.never()
    fun participateById(id: Long, isParticipated: Boolean): Single<Event> = Single.never()
    fun saveEvent(event: Event): Single<Event> = Single.never()
    fun deleteById(id: Long): Single<Unit> = Single.never()
}