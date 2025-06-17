package com.example.w3wthemovie.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DefaultDataStorage(
    private val dataStore: DataStore<Preferences>,
): DataStorage {
    private val trendMovieSyncTimePreferencesKey = longPreferencesKey("trend_movie_sync_time")

    override suspend fun setTrendMovieSyncTime(timestamp: Long)  {
        dataStore.edit { preferences ->
            preferences[trendMovieSyncTimePreferencesKey] = timestamp
        }
    }

    override fun getTrendMovieSyncTime(): Flow<Long?> = dataStore.data.map { preferences ->
        preferences[trendMovieSyncTimePreferencesKey]
    }
}
