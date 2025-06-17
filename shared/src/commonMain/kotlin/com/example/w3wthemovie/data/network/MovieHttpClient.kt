package com.example.w3wthemovie.data.network

import com.example.w3wthemovie.data.network.model.MovieDetailResponse
import com.example.w3wthemovie.data.network.model.MovieResponse

interface MovieHttpClient {
    suspend fun fetchTrendingMovies(): MovieResponse
    suspend fun searchMovies(searchValue: String): MovieResponse
    suspend fun fetchMovieDetail(id: Long): MovieDetailResponse
}