package com.example.w3wthemovie

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.w3wthemovie.data.local.DefaultDataStorage
import com.example.w3wthemovie.data.local.DatabaseDriverFactory
import com.example.w3wthemovie.data.local.DefaultMovieDatabase
import com.example.w3wthemovie.data.network.DefaultMovieHttpClient
import com.example.w3wthemovie.data.repository.TheMovieRepository
import com.example.w3wthemovie.domain.ValidateCacheTimeUseCase

class AppContainer(
    databaseDriverFactory: DatabaseDriverFactory,
    dataStore: DataStore<Preferences>,
) {
    private val theMovieHttpClient by lazy {
        DefaultMovieHttpClient()
    }

    private val movieDatabase by lazy {
        DefaultMovieDatabase(databaseDriverFactory)
    }

    private val validateCacheTimeUseCase by lazy {
        ValidateCacheTimeUseCase()
    }

    private val dataStorage by lazy {
        DefaultDataStorage(dataStore)
    }

    val theMovieRepository by lazy {
        TheMovieRepository(
            httpClient = theMovieHttpClient,
            movieDatabase = movieDatabase,
            dataStorage = dataStorage,
            validateCacheTimeUseCase = validateCacheTimeUseCase,
        )
    }
}
