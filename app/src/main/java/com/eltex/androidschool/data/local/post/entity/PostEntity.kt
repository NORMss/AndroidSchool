package com.eltex.androidschool.data.local.post.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.eltex.androidschool.domain.model.Attachment
import com.eltex.androidschool.domain.model.Coordinates
import com.eltex.androidschool.domain.model.Post
import com.eltex.androidschool.utils.constants.Db.POST_TABLE
import kotlinx.datetime.Instant


@Entity(tableName = POST_TABLE)
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    val id: Long,
    @ColumnInfo("authorId")
    val authorId: Long,
    @ColumnInfo("author")
    val author: String,
    @ColumnInfo("authorJob")
    val authorJob: String,
    @ColumnInfo("authorAvatar")
    val authorAvatar: String?,
    @ColumnInfo("content")
    val content: String,
    @ColumnInfo("published")
    val published: Instant,
    @ColumnInfo("coordinates")
    val coordinates: Coordinates?,
    @ColumnInfo("link")
    val link: String?,
    @ColumnInfo("mentionedMe")
    val mentionedMe: Boolean,
    @ColumnInfo("likedByMe")
    val likedByMe: Boolean,
    @ColumnInfo("attachment")
    val attachment: Attachment?,
) {
    companion object {
        fun fromPost(post: Post): PostEntity = with(post) {
            PostEntity(
                id = id,
                authorId = authorId,
                author = author,
                authorJob = authorJob,
                authorAvatar = authorAvatar,
                content = content,
                published = published,
                coordinates = coordinates,
                link = link,
                mentionedMe = mentionedMe,
                likedByMe = likedByMe,
                attachment = attachment,
            )
        }

    }

    fun toPost(): Post = Post(
        id = id,
        authorId = authorId,
        author = author,
        authorJob = authorJob,
        authorAvatar = authorAvatar,
        content = content,
        published = published,
        coordinates = coordinates,
        link = link,
        mentionedMe = mentionedMe,
        likedByMe = likedByMe,
        attachment = attachment,
    )
}