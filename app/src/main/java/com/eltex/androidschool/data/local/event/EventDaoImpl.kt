package com.eltex.androidschool.data.local.event

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.eltex.androidschool.domain.model.Attachment
import com.eltex.androidschool.domain.model.Coordinates
import com.eltex.androidschool.domain.model.Event
import com.eltex.androidschool.domain.model.EventType
import com.eltex.androidschool.utils.db.CursorExtensionsThrow.getBooleanOrThrow
import com.eltex.androidschool.utils.db.CursorExtensionsThrow.getLongOrThrow
import com.eltex.androidschool.utils.db.CursorExtensionsThrow.getStringOrThrow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.datetime.Instant
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class EventDaoImpl(private val db: SQLiteDatabase) : EventDao {

    private val _eventsFlow = MutableSharedFlow<List<Event>>(replay = 1)

    init {
        loadEventsFromDb()
    }

    override fun getEvents(): Flow<List<Event>> = _eventsFlow

    override suspend fun getEvent(id: Long): Event? {
        var event: Event? = null
        val cursor = db.query(
            EventTable.TABLE_NAME,
            EventTable.allColumns(),
            "${EventTable.ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )
        cursor.use {
            if (it.moveToFirst()) {
                event = it.readEvent()
            }
        }
        return event
    }

    override suspend fun addEvent(event: Event) {
        val contentValues = ContentValues().apply {
            put(EventTable.AUTHOR_ID, event.authorId)
            put(EventTable.AUTHOR, event.author)
            put(EventTable.AUTHOR_JOB, event.authorJob)
            put(EventTable.AUTHOR_AVATAR, event.authorAvatar)
            put(EventTable.CONTENT, event.content)
            put(EventTable.DATETIME, event.datetime.toString())
            put(EventTable.PUBLISHED, event.published.toString())
            put(EventTable.COORDINATES, event.coords?.let { serializeCoordinates(it) })
            put(EventTable.TYPE, event.type.name)
            put(EventTable.LIKE_OWNER_IDS, serializeIds(event.likeOwnerIds))
            put(EventTable.LIKED_BY_ME, if (event.likedByMe) 1 else 0)
            put(EventTable.SPEAKER_IDS, serializeIds(event.speakerIds))
            put(EventTable.PARTICIPANTS_IDS, serializeIds(event.participantsIds))
            put(EventTable.PARTICIPATED_BY_ME, if (event.participatedByMe) 1 else 0)
            put(EventTable.ATTACHMENT, event.attachment?.let { serializeAttachment(it) })
            put(EventTable.LINK, event.link)
            put(EventTable.USERS, Json.encodeToString(event.users))
        }
        db.insert(EventTable.TABLE_NAME, null, contentValues)
        loadEventsFromDb()
    }

    override suspend fun deleteEvent(id: Long) {
        db.delete(
            EventTable.TABLE_NAME,
            "${EventTable.ID} = ?",
            arrayOf(id.toString())
        )
        loadEventsFromDb()
    }

    override suspend fun updateEvent(id: Long, event: Event) {
        val contentValues = ContentValues().apply {
            put(EventTable.CONTENT, event.content)
            put(EventTable.DATETIME, event.datetime.toString())
            put(EventTable.COORDINATES, event.coords?.let { serializeCoordinates(it) })
            put(EventTable.TYPE, event.type.name)
            put(EventTable.LIKE_OWNER_IDS, serializeIds(event.likeOwnerIds))
            put(EventTable.LIKED_BY_ME, if (event.likedByMe) 1 else 0)
            put(EventTable.SPEAKER_IDS, serializeIds(event.speakerIds))
            put(EventTable.PARTICIPANTS_IDS, serializeIds(event.participantsIds))
            put(EventTable.PARTICIPATED_BY_ME, if (event.participatedByMe) 1 else 0)
            put(EventTable.ATTACHMENT, event.attachment?.let { serializeAttachment(it) })
            put(EventTable.LINK, event.link)
            put(EventTable.USERS, Json.encodeToString(event.users))
        }
        db.update(
            EventTable.TABLE_NAME,
            contentValues,
            "${EventTable.ID} = ?",
            arrayOf(id.toString())
        )
        loadEventsFromDb()
    }

    private fun loadEventsFromDb() {
        val cursor = db.query(
            EventTable.TABLE_NAME,
            EventTable.allColumns(),
            null,
            null,
            null,
            null,
            "${EventTable.PUBLISHED} DESC"
        )
        val events = mutableListOf<Event>()
        cursor.use {
            while (it.moveToNext()) {
                events.add(it.readEvent())
            }
        }
        _eventsFlow.tryEmit(events)
    }

    private fun Cursor.readEvent(): Event = Event(
        id = getLongOrThrow(EventTable.ID),
        authorId = getLongOrThrow(EventTable.AUTHOR_ID),
        author = getStringOrThrow(EventTable.AUTHOR)!!,
        authorJob = getStringOrThrow(EventTable.AUTHOR_JOB),
        authorAvatar = getStringOrThrow(EventTable.AUTHOR_AVATAR),
        content = getStringOrThrow(EventTable.CONTENT)!!,
        datetime = Instant.parse(getStringOrThrow(EventTable.DATETIME)!!),
        published = Instant.parse(getStringOrThrow(EventTable.PUBLISHED)!!),
        coords = getStringOrThrow(EventTable.COORDINATES)?.let { deserializeCoordinates(it) },
        type = EventType.valueOf(getStringOrThrow(EventTable.TYPE)!!),
        likeOwnerIds = deserializeIds(getStringOrThrow(EventTable.LIKE_OWNER_IDS)!!),
        likedByMe = getBooleanOrThrow(EventTable.LIKED_BY_ME),
        speakerIds = deserializeIds(getStringOrThrow(EventTable.SPEAKER_IDS)!!),
        participantsIds = deserializeIds(getStringOrThrow(EventTable.PARTICIPANTS_IDS)!!),
        participatedByMe = getBooleanOrThrow(EventTable.PARTICIPATED_BY_ME),
        attachment = getStringOrThrow(EventTable.ATTACHMENT)?.let { deserializeAttachment(it) },
        link = getStringOrThrow(EventTable.LINK),
        users = Json.decodeFromString(getStringOrThrow(EventTable.USERS)!!)
    )

    private fun serializeCoordinates(coordinates: Coordinates): String =
        Json.encodeToString(coordinates)

    private fun deserializeCoordinates(json: String): Coordinates =
        Json.decodeFromString(json)

    private fun serializeAttachment(attachment: Attachment): String =
        Json.encodeToString(attachment)

    private fun deserializeAttachment(json: String): Attachment =
        Json.decodeFromString(json)

    private fun serializeIds(ids: Set<Long>): String =
        ids.joinToString(",")

    private fun deserializeIds(idsString: String): Set<Long> =
        if (idsString.isEmpty()) emptySet() else idsString.split(",").map { it.toLong() }.toSet()
}