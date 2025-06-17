package com.example.w3wthemovie.data.test

import com.example.w3wthemovie.data.network.model.MovieDetailResponse
import com.example.w3wthemovie.data.network.MovieHttpClient
import com.example.w3wthemovie.data.network.model.MovieResponse

class FakeMovieHttpClient(
    private val movieResponse: MovieResponse,
    private var movieDetailResponse: MovieDetailResponse,
): MovieHttpClient {
    override suspend fun fetchTrendingMovies(): MovieResponse = movieResponse

    override suspend fun searchMovies(searchValue: String): MovieResponse = movieResponse

    override suspend fun fetchMovieDetail(id: Long): MovieDetailResponse = movieDetailResponse

    fun updateMovieDetail(movieDetailResponse: MovieDetailResponse) {
        this.movieDetailResponse = movieDetailResponse
    }
}
