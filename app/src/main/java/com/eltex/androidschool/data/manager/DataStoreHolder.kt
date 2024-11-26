package com.eltex.androidschool.data.manager

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized
import kotlin.concurrent.Volatile

@OptIn(InternalCoroutinesApi::class)
object DataStoreHolder {
    @Volatile
    private var INSTANCE: DataStore<Preferences>? = null

    fun getInstance(context: Context, fileName: String): DataStore<Preferences> =
        INSTANCE ?: synchronized(this) {
            INSTANCE ?: PreferenceDataStoreFactory.create(
                produceFile = {
                    context.applicationContext.preferencesDataStoreFile(fileName)
                }
            ).also {
                INSTANCE = it
            }
        }
}