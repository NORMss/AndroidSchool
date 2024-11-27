package com.eltex.androidschool.data.local

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteColumn
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.AutoMigrationSpec
import com.eltex.androidschool.data.local.converter.AttachmentTypeConverter
import com.eltex.androidschool.data.local.converter.CoordinatesTypeConverter
import com.eltex.androidschool.data.local.converter.InstantTypeConverter
import com.eltex.androidschool.data.local.converter.SetCollectionTypeConverter
import com.eltex.androidschool.data.local.converter.UserPreviewTypeConverter
import com.eltex.androidschool.data.local.event.EventDao
import com.eltex.androidschool.data.local.event.entity.EventEntity
import com.eltex.androidschool.data.local.post.PostDao
import com.eltex.androidschool.data.local.post.entity.PostEntity
import com.eltex.androidschool.utils.constants.Db.DB_NAME
import com.eltex.androidschool.utils.constants.Db.EVENT_TABLE


@Database(
    entities = [PostEntity::class, EventEntity::class],
    version = 3,
    autoMigrations = [
        AutoMigration(
            from = 1, to = 2
        ),
        AutoMigration(
            from = 2, to = 3,
            spec = AppDb.DeleteViewFromEvent::class
        )]
)
@TypeConverters(
    AttachmentTypeConverter::class,
    CoordinatesTypeConverter::class,
    UserPreviewTypeConverter::class,
    InstantTypeConverter::class,
    SetCollectionTypeConverter::class,
)
abstract class AppDb : RoomDatabase() {
    abstract val postDao: PostDao
    abstract val eventDao: EventDao

    @DeleteColumn.Entries(
        DeleteColumn(tableName = EVENT_TABLE, columnName = "views")
    )
    class DeleteViewFromEvent : AutoMigrationSpec

    companion object {
        @Volatile
        private var instance: AppDb? = null

        fun getInstance(context: Context): AppDb {
            val application = context.applicationContext


            instance?.let {
                return it
            }

            synchronized(this) {
                instance?.let { return it }
            }

            val dbHelper = Room.databaseBuilder(
                application,
                AppDb::class.java,
                DB_NAME,
            )
                .createFromAsset("app_db.db")
                .addTypeConverter(AttachmentTypeConverter())
                .addTypeConverter(CoordinatesTypeConverter())
                .addTypeConverter(UserPreviewTypeConverter())
                .addTypeConverter(SetCollectionTypeConverter())
                .addTypeConverter(InstantTypeConverter())
                .fallbackToDestructiveMigration()
                .build()

            instance = dbHelper

            return dbHelper
        }

    }
}