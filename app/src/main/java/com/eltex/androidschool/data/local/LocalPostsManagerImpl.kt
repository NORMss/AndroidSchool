package com.eltex.androidschool.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import com.eltex.androidschool.domain.local.LocalPostsManager
import com.eltex.androidschool.domain.model.Post
import com.eltex.androidschool.utils.constants.DataStoreConfig.POSTS_FILE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class LocalPostsManagerImpl(
    context: Context,
    private val  dataStore: DataStore<Preferences>,

) : LocalPostsManager {
    override suspend fun generateNextId(): Long {
        val nextId = getNextId().first() + 1
        saveNextId(nextId)
        return nextId
    }

    override suspend fun addPost(post: Post) {
        val posts = getPostsList()

        savePostsToFile(posts + post)
    }

    override suspend fun updatePost(id: Long, update: (Post) -> Post) {
        val posts = getPostsList()
        val updatedPosts = posts.map {
            if (it.id == id) update(it) else it
        }
        savePostsToFile(updatedPosts)
    }

    override fun getPosts(): Flow<List<Post>> = flow {
        emit(getPostsList())
    }

    override suspend fun deletePost(id: Long) {
        val posts = getPostsList()
        val updatedPosts = posts.filter { it.id != id }
        savePostsToFile(updatedPosts)
    }

    private fun getPostsList(): List<Post> {
        return if (file.exists()) {
            file.bufferedReader().use { reader ->
                Json.decodeFromString(reader.readText())
            }
        } else {
            emptyList()
        }
    }

    private suspend fun saveNextId(nextId: Long) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.NEXT_ID] = nextId
        }
    }

    private fun getNextId(): Flow<Long> {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKey.NEXT_ID] ?: 0L
        }
    }

    private fun savePostsToFile(posts: List<Post>) {
        file.bufferedWriter().use { writer ->
            writer.write(Json.encodeToString(posts))
        }
    }

//    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = POST_CONFIG)
    private val file = context.filesDir.resolve("$POSTS_FILE.json")

    private object PreferencesKey {
        val NEXT_ID = longPreferencesKey("post_next_id")
    }
}