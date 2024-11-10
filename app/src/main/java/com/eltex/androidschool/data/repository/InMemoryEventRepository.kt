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

class InMemoryEventRepository : EventRepository {
    private val _state = MutableStateFlow(
        Event(
            id = 1L,
            authorId = 1001L,
            author = "Sergey Bezborodov",
            authorJob = "Android Developer",
            authorAvatar = null,
            content = "Сегодня поделюсь своим опытом работы с Jetpack Compose!",
            datetime = "2024-11-05T14:30:00",
            published = "2024-11-05T14:30:00",
            coords = Coordinates(lat = 55.7558, long = 37.6176),
            type = EventType.ONLINE,
            likeOwnerIds = emptySet(),
            likedByMe = false,
            speakerIds = setOf(1001L),
            participantsIds = emptySet(),
            participatedByMe = false,
            attachment = Attachment(
                url = "https://samplelib.com/lib/preview/mp3/sample-6s.mp3",
                type = AttachmentType.AUDIO
            ),
            link = "https://example.com/article",
            users = listOf(
                UserPreview(
                    avatar = "https://avatars.githubusercontent.com/u/47896309?v=4",
                    name = "Sergey Bezborodov"
                )
            )
        )

    )

    override fun getEvent(): Flow<Event> {
        return _state.asStateFlow()
    }

    override fun like() {
        _state.update {
            it.copy(likedByMe = !it.likedByMe)
        }
    }

    override fun participate() {
        _state.update {
            it.copy(participatedByMe = !it.participatedByMe)
        }
    }
}