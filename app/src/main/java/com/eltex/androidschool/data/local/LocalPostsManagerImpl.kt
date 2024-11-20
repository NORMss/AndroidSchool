package com.eltex.androidschool.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import com.eltex.androidschool.domain.local.LocalPostsManager
import com.eltex.androidschool.domain.model.Post
import kotlinx.coroutines.flow.Flow
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import com.eltex.androidschool.utils.constants.DataStoreConfig.NEXT_ID
import com.eltex.androidschool.utils.constants.DataStoreConfig.POSTS_FILE_NAME
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class LocalPostsManagerImpl(
    private val context: Context,
) : LocalPostsManager {
    override suspend fun generateNextId(): Long {
        val nextId = getNextId().first() + 1 // Получаем текущий ID и увеличиваем на 1
        saveNextId(nextId)
        return nextId
    }

    override suspend fun savePost(post: Post) {
        file.bufferedWriter()
            .use {
                it.write(Json.encodeToString(post))
            }
    }

    override suspend fun updatePost(post: Post) {
        val posts = getPostsList()
        val updatedPosts = posts.map {
            if (it.id == post.id) post else it
        }
        savePostsToFile(updatedPosts)
    }

    override fun getPosts(): Flow<List<Post>> = flow {
        getPostsList()
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
        context.dataStore.edit { preferences ->
            preferences[PreferencesKey.NEXT_ID] = nextId
        }
    }

    private fun getNextId(): Flow<Long> {
        return context.dataStore.data.map { preferences ->
            preferences[PreferencesKey.NEXT_ID] ?: 0L
        }
    }

    private fun savePostsToFile(posts: List<Post>) {
        file.bufferedWriter().use { writer ->
            writer.write(Json.encodeToString(posts))
        }
    }

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = NEXT_ID)
    private val file = context.filesDir.resolve(POSTS_FILE_NAME)

    private object PreferencesKey {
        val NEXT_ID = longPreferencesKey("next_id")
    }
}