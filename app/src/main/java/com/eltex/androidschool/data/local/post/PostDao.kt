package com.eltex.androidschool.data.local.post

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.eltex.androidschool.data.local.post.entity.PostEntity
import com.eltex.androidschool.utils.constants.Db.POST_TABLE
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {
    @Query("SELECT * FROM $POST_TABLE")
    fun getPosts(): Flow<List<PostEntity>>

    @Query("SELECT * FROM $POST_TABLE WHERE id = :id")
    suspend fun getPost(id: Long): PostEntity?

    @Upsert
    suspend fun savePost(post: PostEntity)

    @Query("DELETE FROM $POST_TABLE WHERE id = :id")
    suspend fun deletePost(id: Long)
}