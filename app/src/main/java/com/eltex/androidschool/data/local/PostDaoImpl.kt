package com.eltex.androidschool.data.local

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.eltex.androidschool.domain.model.Attachment
import com.eltex.androidschool.domain.model.Coordinates
import com.eltex.androidschool.domain.model.Post
import com.eltex.androidschool.utils.db.CursorExtensionsThrow.getBooleanOrThrow
import com.eltex.androidschool.utils.db.CursorExtensionsThrow.getLongOrThrow
import com.eltex.androidschool.utils.db.CursorExtensionsThrow.getStringOrThrow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.datetime.Instant
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class PostDaoImpl(private val db: SQLiteDatabase) : PostDao {

    private val _postsFlow = MutableSharedFlow<List<Post>>(replay = 1)

    init {
        loadPostsFromDb()
    }

    override fun getPosts(): Flow<List<Post>> = _postsFlow

    override suspend fun getPost(id: Long): Post? {
        var post: Post? = null
        val cursor = db.query(
            PostTable.TABLE_NAME,
            null,
            "${PostTable.ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )
        cursor.use {
            if (it.moveToFirst()) {
                post = it.readPost()
            }
        }
        return post
    }

    override suspend fun addPost(post: Post) {
        val contentValues = ContentValues().apply {
            put(PostTable.AUTHOR_ID, post.authorId)
            put(PostTable.AUTHOR, post.author)
            put(PostTable.AUTHOR_JOB, post.authorJob)
            put(PostTable.AUTHOR_AVATAR, post.authorAvatar)
            put(PostTable.CONTENT, post.content)
            put(PostTable.PUBLISHED, post.published.toString())
            put(PostTable.COORDINATES, post.coordinates?.let { serializeCoordinates(it) })
            put(PostTable.LINK, post.link)
            put(PostTable.MENTIONED_ME, if (post.mentionedMe) 1 else 0)
            put(PostTable.LIKED_BY_ME, if (post.likedByMe) 1 else 0)
            put(PostTable.ATTACHMENT, post.attachment?.let { serializeAttachment(it) })
        }
        db.insert(PostTable.TABLE_NAME, null, contentValues)
        loadPostsFromDb()
    }

    override suspend fun deletePost(id: Long) {
        db.delete(
            PostTable.TABLE_NAME,
            "${PostTable.ID} = ?",
            arrayOf(id.toString())
        )
        loadPostsFromDb()
    }

    override suspend fun updatePost(id: Long, post: Post) {
        val contentValues = ContentValues().apply {
            put(PostTable.CONTENT, post.content)
            put(PostTable.COORDINATES, post.coordinates?.let { serializeCoordinates(it) })
            put(PostTable.LINK, post.link)
            put(PostTable.LIKED_BY_ME, if (post.likedByMe) 1 else 0)
            put(PostTable.MENTIONED_ME, if (post.mentionedMe) 1 else 0)
            put(PostTable.ATTACHMENT, post.attachment?.let { serializeAttachment(it) })
        }
        db.update(
            PostTable.TABLE_NAME,
            contentValues,
            "${PostTable.ID} = ?",
            arrayOf(id.toString())
        )
        loadPostsFromDb()
    }

    private fun loadPostsFromDb() {
        val cursor = db.query(
            PostTable.TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            "${PostTable.PUBLISHED} DESC"
        )
        val posts = mutableListOf<Post>()
        cursor.use {
            while (it.moveToNext()) {
                posts.add(it.readPost())
            }
        }
        _postsFlow.tryEmit(posts)
    }


    private fun Cursor.readPost(): Post = Post(
        id = getLongOrThrow(PostTable.ID),
        authorId = getLongOrThrow(PostTable.AUTHOR_ID),
        author = getStringOrThrow(PostTable.AUTHOR)!!,
        authorJob = getStringOrThrow(PostTable.AUTHOR_JOB)!!,
        authorAvatar = getStringOrThrow(PostTable.AUTHOR_AVATAR),
        content = getStringOrThrow(PostTable.CONTENT)!!,
        published = Instant.parse(getStringOrThrow(PostTable.PUBLISHED)!!),
        coordinates = getStringOrThrow(PostTable.COORDINATES)?.let { deserializeCoordinates(it) },
        link = getStringOrThrow(PostTable.LINK),
        mentionedMe = getBooleanOrThrow(PostTable.MENTIONED_ME),
        likedByMe = getBooleanOrThrow(PostTable.LIKED_BY_ME),
        attachment = getStringOrThrow(PostTable.ATTACHMENT)?.let { deserializeAttachment(it) },
    )

    private fun serializeCoordinates(coordinates: Coordinates): String {
        return Json.encodeToString(coordinates)
    }

    private fun deserializeCoordinates(json: String): Coordinates =
        Json.decodeFromString<Coordinates>(json)


    private fun serializeAttachment(attachment: Attachment): String {
        return Json.encodeToString(attachment)
    }

    private fun deserializeAttachment(json: String): Attachment =
        Json.decodeFromString<Attachment>(json)

    companion object {
        @Volatile
        private var instance: PostDaoImpl? = null

        fun getInstance(context: Context): PostDaoImpl {
            val application = context.applicationContext


            instance?.let {
                return it
            }

            synchronized(this) {
                instance?.let { return it }
            }

            val dbHelper = PostDaoImpl(DbHelper(application).writableDatabase)


            instance = dbHelper

            return dbHelper
        }

    }
}