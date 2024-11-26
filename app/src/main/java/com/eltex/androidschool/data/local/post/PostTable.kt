package com.eltex.androidschool.data.local.post

object PostTable {
    const val TABLE_NAME = "posts"
    const val ID = "id"
    const val AUTHOR_ID = "author_id"
    const val AUTHOR = "author"
    const val AUTHOR_JOB = "author_job"
    const val AUTHOR_AVATAR = "author_avatar"
    const val CONTENT = "content"
    const val PUBLISHED = "published"
    const val COORDINATES = "coordinates"
    const val LINK = "link"
    const val MENTIONED_ME = "mentioned_me"
    const val LIKED_BY_ME = "liked_by_me"
    const val ATTACHMENT = "attachment"

    fun allColumns(): Array<String> {
        return arrayOf(
            ID,
            AUTHOR_ID,
            AUTHOR,
            AUTHOR_JOB,
            AUTHOR_AVATAR,
            CONTENT,
            PUBLISHED,
            COORDINATES,
            LINK,
            MENTIONED_ME,
            LIKED_BY_ME,
            ATTACHMENT,
        )
    }
}
