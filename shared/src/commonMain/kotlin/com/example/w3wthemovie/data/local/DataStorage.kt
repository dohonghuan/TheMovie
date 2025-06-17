package com.example.w3wthemovie.data.local

import kotlinx.coroutines.flow.Flow

interface DataStorage {
    suspend fun setTrendMovieSyncTime(timestamp: Long)
    fun getTrendMovieSyncTime(): Flow<Long?>
}