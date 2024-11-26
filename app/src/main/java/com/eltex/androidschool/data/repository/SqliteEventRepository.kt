package com.eltex.androidschool.data.repository

import com.eltex.androidschool.data.local.event.EventDao
import com.eltex.androidschool.domain.model.Attachment
import com.eltex.androidschool.domain.model.Coordinates
import com.eltex.androidschool.domain.model.Event
import com.eltex.androidschool.domain.model.EventType
import com.eltex.androidschool.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock

class SqliteEventRepository(
    private val postDao: EventDao
) : EventRepository {

    override fun getEvents(): Flow<List<Event>> = postDao.getEvents()

    override suspend fun likeById(id: Long) {
        val post = postDao.getEvent(id)
        post?.copy(likedByMe = !post.likedByMe)?.let {
            postDao.updateEvent(id, it)
        }
    }

    override suspend fun participateById(id: Long) {
        val post = postDao.getEvent(id)
        post?.copy(participatedByMe = !post.participatedByMe)?.let {
            postDao.updateEvent(id, it)
        }
    }

    override suspend fun deleteEventById(id: Long) {
        postDao.deleteEvent(id)
    }

    override suspend fun editEventById(id: Long, textContent: String) {
        val post = postDao.getEvent(id)
        post?.copy(content = textContent)?.let {
            postDao.updateEvent(id, it)
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
        postDao.addEvent(newEvent)
    }
}