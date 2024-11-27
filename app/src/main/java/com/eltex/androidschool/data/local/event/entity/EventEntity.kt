package com.eltex.androidschool.data.local.event.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.eltex.androidschool.domain.model.Attachment
import com.eltex.androidschool.domain.model.Coordinates
import com.eltex.androidschool.domain.model.Event
import com.eltex.androidschool.domain.model.EventType
import com.eltex.androidschool.domain.model.UserPreview
import com.eltex.androidschool.utils.constants.Db.EVENT_TABLE
import kotlinx.datetime.Instant

@Entity(tableName = EVENT_TABLE)
data class EventEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    val id: Long,
    @ColumnInfo("authorId")
    val authorId: Long,
    @ColumnInfo("author")
    val author: String,
    @ColumnInfo("authorJob")
    val authorJob: String?,
    @ColumnInfo("authorAvatar")
    val authorAvatar: String?,
    @ColumnInfo("content")
    val content: String,
    @ColumnInfo("datetime")
    val datetime: Instant,
    @ColumnInfo("published")
    val published: Instant,
    @ColumnInfo("coords")
    val coords: Coordinates?,
    @ColumnInfo("type")
    val type: EventType,
    @ColumnInfo("likeOwnerIds")
    val likeOwnerIds: Set<Long>,
    @ColumnInfo("likedByMe")
    val likedByMe: Boolean,
    @ColumnInfo("speakerIds")
    val speakerIds: Set<Long>,
    @ColumnInfo("participantsIds")
    val participantsIds: Set<Long>,
    @ColumnInfo("participatedByMe")
    val participatedByMe: Boolean,
    @ColumnInfo("attachment")
    val attachment: Attachment?,
    @ColumnInfo("link")
    val link: String?,
    @ColumnInfo("users")
    val users: List<UserPreview>,
) {
    companion object {
        fun fromEvent(event: Event): EventEntity = with(event) {
            EventEntity(
                id = id,
                authorId = authorId,
                author = author,
                authorJob = authorJob,
                authorAvatar = authorAvatar,
                content = content,
                datetime = datetime,
                published = published,
                coords = coords,
                type = type,
                likeOwnerIds = likeOwnerIds,
                likedByMe = likedByMe,
                speakerIds = speakerIds,
                participantsIds = participantsIds,
                participatedByMe = participatedByMe,
                attachment = attachment,
                link = link,
                users = users,
            )
        }
    }

    fun toEvent() = Event(
        id = id,
        authorId = authorId,
        author = author,
        authorJob = authorJob,
        authorAvatar = authorAvatar,
        content = content,
        datetime = datetime,
        published = published,
        coords = coords,
        type = type,
        likeOwnerIds = likeOwnerIds,
        likedByMe = likedByMe,
        speakerIds = speakerIds,
        participantsIds = participantsIds,
        participatedByMe = participatedByMe,
        attachment = attachment,
        link = link,
        users = users,
    )
}
