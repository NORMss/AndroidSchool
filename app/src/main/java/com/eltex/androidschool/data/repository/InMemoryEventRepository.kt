package com.eltex.androidschool.data.repository

import com.eltex.androidschool.domain.model.Attachment
import com.eltex.androidschool.domain.model.AttachmentType
import com.eltex.androidschool.domain.model.Coordinates
import com.eltex.androidschool.domain.model.Event
import com.eltex.androidschool.domain.model.EventType
import com.eltex.androidschool.domain.model.UserPreview
import com.eltex.androidschool.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.Clock

class InMemoryEventRepository : EventRepository {
    private var nextId = 0L
    private var authorId = 1000L
    private val _state = MutableStateFlow(
        listOf(
            Event(
                id = nextId++,
                authorId = authorId++,
                author = "Анна Смирнова",
                authorJob = "Android Developer",
                authorAvatar = "https://randomuser.me/api/portraits/women/44.jpg",
                content = "Сегодня обсуждаем современные подходы в Android-разработке с использованием Jetpack Compose.",
                datetime = "2024-11-18T18:00:00Z",
                published = "2024-11-18T12:00:00Z",
                coords = Coordinates(lat = 55.7522, long = 37.6156),
                type = EventType.ONLINE,
                likeOwnerIds = setOf(1002L, 1003L),
                likedByMe = true,
                speakerIds = setOf(1001L),
                participantsIds = setOf(1002L),
                participatedByMe = true,
                attachment = null,
                link = "https://example.com/jetpack-compose-event",
                users = listOf(
                    UserPreview(
                        avatar = "https://randomuser.me/api/portraits/women/44.jpg",
                        name = "Анна Смирнова"
                    )
                )
            ),
            Event(
                id = nextId++,
                authorId = authorId++,
                author = "Иван Петров",
                authorJob = "Senior Android Developer",
                authorAvatar = null,
                content = "Круглый стол: переход с Java на Kotlin. Разбираем лучшие практики и подводные камни.",
                datetime = "2024-11-18T16:00:00Z",
                published = "2024-11-18T10:00:00Z",
                coords = Coordinates(lat = 55.7558, long = 37.6176),
                type = EventType.OFFLINE,
                likeOwnerIds = emptySet(),
                likedByMe = false,
                speakerIds = setOf(1002L, 1003L),
                participantsIds = setOf(1004L, 1005L),
                participatedByMe = false,
                attachment = null,
                link = "https://example.com/kotlin-roundtable",
                users = listOf(
                    UserPreview(
                        avatar = "https://randomuser.me/api/portraits/men/45.jpg",
                        name = "Иван Петров"
                    )
                )
            ),
            Event(
                id = nextId++,
                authorId = authorId++,
                author = "Мария Кузнецова",
                authorJob = "Middle Android Developer",
                authorAvatar = "https://randomuser.me/api/portraits/women/33.jpg",
                content = "Как провести рефакторинг старого приложения и не потерять пользователей?",
                datetime = "2024-11-17T14:00:00Z",
                published = "2024-11-17T08:00:00Z",
                coords = Coordinates(lat = 56.8389, long = 60.6057),
                type = EventType.ONLINE,
                likeOwnerIds = setOf(1001L, 1004L),
                likedByMe = true,
                speakerIds = setOf(1003L),
                participantsIds = setOf(1001L),
                participatedByMe = true,
                attachment = Attachment(
                    url = "https://samplelib.com/lib/preview/mp3/sample-6s.mp3",
                    type = AttachmentType.AUDIO
                ),
                link = "https://example.com/refactoring-legacy-code",
                users = listOf(
                    UserPreview(
                        avatar = "https://randomuser.me/api/portraits/women/33.jpg",
                        name = "Мария Кузнецова"
                    )
                )
            ),
            Event(
                id = nextId++,
                authorId = authorId++,
                author = "Дмитрий Иванов",
                authorJob = "Junior Android Developer",
                authorAvatar = null,
                content = "Интерактивный воркшоп по использованию Coroutines в Android.",
                datetime = "2024-11-15T16:00:00Z",
                published = "2024-11-15T10:00:00Z",
                coords = Coordinates(lat = 55.7558, long = 37.6176),
                type = EventType.OFFLINE,
                likeOwnerIds = setOf(1002L),
                likedByMe = false,
                speakerIds = setOf(1004L),
                participantsIds = emptySet(),
                participatedByMe = false,
                attachment = Attachment(
                    url = "https://miro.medium.com/v2/resize:fit:580/0*9UfWHZzkng14RGXh.png",
                    type = AttachmentType.IMAGE
                ),
                link = "https://example.com/coroutines-workshop",
                users = listOf()
            ),
            Event(
                id = nextId++,
                authorId = authorId++,
                author = "Алексей Сидоров",
                authorJob = "Android Lead",
                authorAvatar = "https://randomuser.me/api/portraits/men/22.jpg",
                content = "Обзор трендов 2024 в Android-разработке: что ждет нас в будущем?",
                datetime = "2024-11-15T20:00:00Z",
                published = "2024-11-15T14:00:00Z",
                coords = Coordinates(lat = 59.9343, long = 30.3351),
                type = EventType.ONLINE,
                likeOwnerIds = setOf(1001L, 1003L),
                likedByMe = true,
                speakerIds = setOf(1005L),
                participantsIds = setOf(1001L, 1003L, 1004L),
                participatedByMe = true,
                attachment = null,
                link = "https://example.com/android-trends-2024",
                users = listOf(
                    UserPreview(
                        avatar = "https://randomuser.me/api/portraits/men/22.jpg",
                        name = "Алексей Сидоров"
                    )
                )
            )
        )
    )

    override fun getEvents(): Flow<List<Event>> {
        return _state.asStateFlow()
    }

    override fun likeById(id: Long) {
        _state.update { event ->
            event.map {
                if (it.id == id) {
                    it.copy(
                        likedByMe = !it.likedByMe
                    )
                } else {
                    it
                }
            }
        }
    }

    override fun participateById(id: Long) {
        _state.update { posts ->
            posts.map {
                if (it.id == id) {
                    it.copy(
                        participatedByMe = !it.participatedByMe
                    )
                } else {
                    it
                }
            }
        }
    }

    override fun addPost(textContent: String) {
        _state.update { events ->
            buildList(capacity = events.size + 1) {
                add(
                    Event(
                        id = nextId++,
                        authorId = authorId++,
                        author = "Sergey Bezborodov",
                        authorJob = "Junior Android Developer",
                        authorAvatar = "https://avatars.githubusercontent.com/u/47896309?v=4",
                        content = textContent,
                        datetime = Clock.System.now().toString(),
                        published = Clock.System.now().toString(),
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
                        attachment = null,
                        link = "https://github.com/NORMss",
                        users = emptyList(),
                    )
                )
            }
        }
    }

    override fun deleteById(id: Long) {
        _state.update { events ->
            events.filter {
                it.id != id
            }
        }
    }

    override fun editById(id: Long, textContent: String) {
        _state.update { events ->
            events.map { event ->
                if (event.id == id) {
                    Event(
                        id = id,
                        authorId = event.authorId,
                        author = event.author,
                        authorJob = event.authorJob,
                        authorAvatar = event.authorAvatar,
                        content = textContent,
                        datetime = event.datetime,
                        published = event.published,
                        coords = event.coords,
                        type = event.type,
                        likeOwnerIds = event.likeOwnerIds,
                        likedByMe = event.likedByMe,
                        speakerIds = event.speakerIds,
                        participantsIds = event.participantsIds,
                        participatedByMe = event.participatedByMe,
                        attachment = event.attachment,
                        link = event.link,
                        users = event.users,
                    )
                } else {
                    event
                }
            }
        }
    }
}