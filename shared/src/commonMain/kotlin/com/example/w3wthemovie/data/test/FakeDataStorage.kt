package com.example.w3wthemovie.data.test

import com.example.w3wthemovie.data.local.DataStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class FakeDataStorage: DataStorage {
    private val data: MutableStateFlow<Long?> = MutableStateFlow(null)
    override suspend fun setTrendMovieSyncTime(timestamp: Long) {
        data.update { timestamp }
    }

    override fun getTrendMovieSyncTime(): Flow<Long?> = data

    fun clear() {
        data.update { null }
    }
}
