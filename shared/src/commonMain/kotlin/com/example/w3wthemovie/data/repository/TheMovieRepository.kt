package com.example.w3wthemovie.data.repository

import com.example.w3wthemovie.common.model.MovieDetail
import com.example.w3wthemovie.common.model.Movie
import com.example.w3wthemovie.common.model.dtoMapToMovieDetail
import com.example.w3wthemovie.common.model.entityMapToMovies
import com.example.w3wthemovie.common.model.dtoMapToMovies
import com.example.w3wthemovie.common.model.entityMapToMovieDetail
import com.example.w3wthemovie.common.model.mapToMovieDetailTable
import com.example.w3wthemovie.data.local.DataStorage
import com.example.w3wthemovie.data.local.MovieDatabase
import com.example.w3wthemovie.data.network.MovieHttpClient
import com.example.w3wthemovie.domain.ValidateCacheTimeUseCase
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.datetime.Clock

class TheMovieRepository(
    private val httpClient: MovieHttpClient,
    private val movieDatabase: MovieDatabase,
    private val dataStorage: DataStorage,
    private val validateCacheTimeUseCase: ValidateCacheTimeUseCase,
) {

    @Throws(Throwable::class)
    suspend fun fetchTrendingMovies(): List<Movie>  {
        val syncTime = dataStorage.getTrendMovieSyncTime().firstOrNull()
        return if (syncTime == null || !validateCacheTimeUseCase(syncTime)) {
            val response = httpClient.fetchTrendingMovies()
            movieDatabase.clearAndInsertMovies(response.results)
            dataStorage.setTrendMovieSyncTime(Clock.System.now().epochSeconds)
            movieDatabase.getAllTrendMovie().entityMapToMovies()
        } else {
            movieDatabase.getAllTrendMovie().entityMapToMovies()
        }
    }

    @Throws(Throwable::class)
    suspend fun searchMovies(searchValue: String): List<Movie> =
        httpClient.searchMovies(searchValue).results.dtoMapToMovies()

    @Throws(Throwable::class)
    suspend fun fetchMovieDetail(id: Long): MovieDetail {
        val cachedDetail = movieDatabase.getDetail(id)
        return if (cachedDetail == null || !validateCacheTimeUseCase(cachedDetail.syncTime)) {
            val response = httpClient.fetchMovieDetail(id)
            val detail = response.dtoMapToMovieDetail().mapToMovieDetailTable(Clock.System.now().epochSeconds)
            movieDatabase.insertOrUpdateDetail(detail)
            detail.entityMapToMovieDetail()
        } else {
            cachedDetail.entityMapToMovieDetail()
        }
    }
}
