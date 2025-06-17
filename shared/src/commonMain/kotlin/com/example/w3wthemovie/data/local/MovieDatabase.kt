package com.example.w3wthemovie.data.local

import com.example.w3wthemovie.MovieDetailTable
import com.example.w3wthemovie.TrendMovie
import com.example.w3wthemovie.data.network.model.MovieDto

interface MovieDatabase {
    fun clearAndInsertMovies(movies: List<MovieDto>)
    fun getAllTrendMovie(): List<TrendMovie>
    fun insertOrUpdateDetail(detail: MovieDetailTable)
    fun getDetail(id: Long): MovieDetailTable?
}
