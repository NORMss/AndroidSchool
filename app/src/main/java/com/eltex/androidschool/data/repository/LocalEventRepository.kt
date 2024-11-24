import com.eltex.androidschool.domain.local.LocalEventManager
import com.eltex.androidschool.domain.model.Attachment
import com.eltex.androidschool.domain.model.Coordinates
import com.eltex.androidschool.domain.model.Event
import com.eltex.androidschool.domain.model.EventType
import com.eltex.androidschool.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock

class LocalEventRepository(
    private val localEventManager: LocalEventManager,
) : EventRepository {
    override fun getEvents(): Flow<List<Event>> =
        localEventManager.getEvents()

    override suspend fun likeById(id: Long) {
        localEventManager.updateEvent(id) {
            it.copy(likedByMe = !it.likedByMe)
        }
    }

    override suspend fun participateById(id: Long) {
        localEventManager.updateEvent(id) {
            it.copy(participatedByMe = !it.participatedByMe)
        }
    }

    override suspend fun deleteEventById(id: Long) {
        localEventManager.deleteEvent(id)
    }

    override suspend fun addEvent(textContent: String, attachment: Attachment?, link: String?) {
        localEventManager.addEvent(
            Event(
                id = localEventManager.generateNextId(),
                authorId = 1000,
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
                attachment = attachment,
                link = link,
                users = emptyList(),
            )
        )
    }

    override suspend fun editEventById(id: Long, textContent: String) {
        localEventManager.updateEvent(id) { event ->
            event.copy(content = textContent)
        }
    }
}