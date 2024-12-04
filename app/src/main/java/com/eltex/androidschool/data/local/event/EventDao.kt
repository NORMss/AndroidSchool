package com.eltex.androidschool.data.local.event

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.eltex.androidschool.data.local.event.entity.EventEntity
import com.eltex.androidschool.utils.constants.Db.EVENT_TABLE
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {
    @Query("DELETE FROM $EVENT_TABLE WHERE id = :id")
    suspend fun deleteEvent(id: Long)

    @Upsert
    suspend fun saveEvent(event: EventEntity)

    @Query("SELECT * FROM $EVENT_TABLE WHERE id = :id")
    suspend fun getEvent(id: Long): EventEntity

    @Query("SELECT * FROM $EVENT_TABLE")
    fun getEvents(): Flow<List<EventEntity>>
}