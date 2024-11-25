package com.eltex.androidschool.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import com.eltex.androidschool.domain.local.LocalPostsManager
import com.eltex.androidschool.domain.model.Post
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class LocalPostsManagerImpl(
    private val dataStore: DataStore<Preferences>,
    private val file: File,
) : LocalPostsManager {

    private val _postsFlow = MutableStateFlow(getPostsList())
    private val postsFlow = _postsFlow.asStateFlow()

    override suspend fun generateNextId(): Long {
        val nextId = getNextId().first() + 1
        saveNextId(nextId)
        return nextId
    }

    override suspend fun addPost(post: Post) {
        val posts = getPostsList()
        val newList = posts + post
        savePostsToFile(newList)
        notifyListeners(newList)
    }

    override suspend fun updatePost(id: Long, update: (Post) -> Post) {
        val posts = getPostsList()
        val updatedPosts = posts.map { if (it.id == id) update(it) else it }
        savePostsToFile(updatedPosts)
        notifyListeners(updatedPosts)
    }

    override fun getPosts(): Flow<List<Post>> = postsFlow

    override suspend fun deletePost(id: Long) {
        val posts = getPostsList()
        val updatedPosts = posts.filter { it.id != id }
        savePostsToFile(updatedPosts)
        notifyListeners(updatedPosts)
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

    private fun notifyListeners(updatedPosts: List<Post>) {
        _postsFlow.value = updatedPosts
    }

    private object PreferencesKey {
        val NEXT_ID = longPreferencesKey("next_id")
    }
}
