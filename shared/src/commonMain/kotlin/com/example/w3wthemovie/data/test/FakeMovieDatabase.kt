package com.example.w3wthemovie.data.test

import com.example.w3wthemovie.MovieDetailTable
import com.example.w3wthemovie.TrendMovie
import com.example.w3wthemovie.data.local.MovieDatabase
import com.example.w3wthemovie.data.network.model.MovieDto

class FakeMovieDatabase: MovieDatabase {
    private val trendMovies: MutableList<TrendMovie> = mutableListOf()
    private val detailMovies: MutableList<MovieDetailTable> = mutableListOf()
    override fun clearAndInsertMovies(movies: List<MovieDto>) {
        trendMovies.clear()
        trendMovies.addAll(movies.map {
            TrendMovie(
                id = it.id,
                title = it.title,
                image = it.posterPath,
                year = it.releaseDate,
                rating = it.voteAverage,
            )
        })
    }

    override fun getAllTrendMovie(): List<TrendMovie> = trendMovies

    override fun insertOrUpdateDetail(detail: MovieDetailTable) {
        val index = detailMovies.indexOfFirst { it.id == detail.id }
        if (index != -1) {
            detailMovies[index] = detail
        } else {
            detailMovies.add(detail)
        }
    }

    override fun getDetail(id: Long): MovieDetailTable? = detailMovies.find { it.id == id }

    fun clear() {
        trendMovies.clear()
        detailMovies.clear()
    }

}