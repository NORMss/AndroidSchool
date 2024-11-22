package com.eltex.androidschool.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import com.eltex.androidschool.domain.local.LocalEventManager
import com.eltex.androidschool.domain.model.Event
import com.eltex.androidschool.utils.constants.DataStoreConfig.EVENTS_FILE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class LocalEventManagerImpl(
    context: Context,
    private val dataStore: DataStore<Preferences>
) : LocalEventManager {
    override suspend fun generateNextId(): Long {
        val nextId = getNextId().first() + 1
        saveNextId(nextId)
        return nextId
    }

    override suspend fun addEvent(event: Event) {
        val events = getEventsList()

        saveEventsToFile(events + event)
    }

    override suspend fun updateEvent(id: Long, update: (Event) -> Event) {
        val events = getEventsList()
        val updatedEvents = events.map {
            if (it.id == id) update(it) else it
        }
        saveEventsToFile(updatedEvents)
    }

    override fun getEvents(): Flow<List<Event>> = flow {
        emit(getEventsList())
    }

    override suspend fun deleteEvent(id: Long) {
        val events = getEventsList()
        val updatedEvents = events.filter { it.id != id }
        saveEventsToFile(updatedEvents)
    }

    private fun getEventsList(): List<Event> {
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

    private fun saveEventsToFile(events: List<Event>) {
        file.bufferedWriter().use { writer ->
            writer.write(Json.encodeToString(events))
        }
    }

    //    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = EVENT_CONFIG)
    private val file = context.applicationContext.filesDir.resolve("$EVENTS_FILE.json")

    private object PreferencesKey {
        val NEXT_ID = longPreferencesKey("next_id")
    }
}