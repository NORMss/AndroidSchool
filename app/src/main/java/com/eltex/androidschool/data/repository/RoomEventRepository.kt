package com.eltex.androidschool.data.repository

import com.eltex.androidschool.data.local.event.EventDao
import com.eltex.androidschool.data.local.event.entity.EventEntity
import com.eltex.androidschool.domain.model.Attachment
import com.eltex.androidschool.domain.model.Coordinates
import com.eltex.androidschool.domain.model.Event
import com.eltex.androidschool.domain.model.EventType
import com.eltex.androidschool.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock

class RoomEventRepository(
    private val eventDao: EventDao
) : EventRepository {

    override fun getEvents(): Flow<List<Event>> =
        eventDao.getEvents().map { it.map(EventEntity::toEvent) }

    override suspend fun likeById(id: Long) {
        val post = eventDao.getEvent(id)
        post?.copy(likedByMe = !post.likedByMe)?.let {
            eventDao.saveEvent(it)
        }
    }

    override suspend fun participateById(id: Long) {
        val event = eventDao.getEvent(id)
        event?.copy(participatedByMe = !event.participatedByMe)?.let {
            eventDao.saveEvent(it)
        }
    }

    override suspend fun deleteEventById(id: Long) {
        eventDao.deleteEvent(id)
    }

    override suspend fun editEventById(id: Long, textContent: String) {
        val post = eventDao.getEvent(id)
        post?.copy(content = textContent)?.let {
            eventDao.saveEvent(it)
        }
    }

    override suspend fun addEvent(textContent: String, attachment: Attachment?, link: String?) {
        val newEvent = Event(
            id = 0,
            authorId = 1000,
            author = "Sergey Bezborodov",
            authorJob = "Junior Android Developer",
            authorAvatar = "https://avatars.githubusercontent.com/u/47896309?v=4",
            content = textContent,
            datetime = Clock.System.now(),
            published = Clock.System.now(),
            coords = Coordinates(
                lat = 54.9833,
                long = 82.8964,
            ),
            type = EventType.ONLINE,
            likeOwnerIds = emptySet(),
            likedByMe = false,
            speakerIds = emptySet(),
            participantsIds = emptySet(),
            participatedByMe = false,
            attachment = attachment,
            link = link,
            users = emptyList(),
        )
        eventDao.saveEvent(EventEntity.fromEvent(newEvent))
    }
}