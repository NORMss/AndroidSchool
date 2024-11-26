package com.eltex.androidschool.data.local.event

object EventTable {
    const val TABLE_NAME = "events"
    const val ID = "id"
    const val AUTHOR_ID = "author_id"
    const val AUTHOR = "author"
    const val AUTHOR_JOB = "author_job"
    const val AUTHOR_AVATAR = "author_avatar"
    const val CONTENT = "content"
    const val DATETIME = "datetime"
    const val PUBLISHED = "published"
    const val COORDINATES = "coordinates"
    const val TYPE = "type"
    const val LIKE_OWNER_IDS = "like_owner_ids"
    const val LIKED_BY_ME = "liked_by_me"
    const val SPEAKER_IDS = "speaker_ids"
    const val PARTICIPANTS_IDS = "participants_ids"
    const val PARTICIPATED_BY_ME = "participated_by_me"
    const val ATTACHMENT = "attachment"
    const val LINK = "link"
    const val USERS = "users"

    fun allColumns(): Array<String> {
        return arrayOf(
            ID,
            AUTHOR_ID,
            AUTHOR,
            AUTHOR_JOB,
            AUTHOR_AVATAR,
            CONTENT,
            DATETIME,
            PUBLISHED,
            COORDINATES,
            TYPE,
            LIKE_OWNER_IDS,
            LIKED_BY_ME,
            SPEAKER_IDS,
            PARTICIPANTS_IDS,
            PARTICIPATED_BY_ME,
            ATTACHMENT,
            LINK,
            USERS,
        )
    }
}
