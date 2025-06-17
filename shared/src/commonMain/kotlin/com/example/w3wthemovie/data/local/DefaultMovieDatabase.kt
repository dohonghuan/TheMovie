package com.example.w3wthemovie.data.local

import com.example.w3wthemovie.MovieDetailTable
import com.example.w3wthemovie.TrendMovie
import com.example.w3wthemovie.W3WTheMovieDatabase
import com.example.w3wthemovie.common.W3WTheMovieConst
import com.example.w3wthemovie.data.network.model.MovieDto

class DefaultMovieDatabase(
    databaseDriverFactory: DatabaseDriverFactory,
): MovieDatabase {
    private val database = W3WTheMovieDatabase(databaseDriverFactory.createDriver())
    private val trendingQuery = database.trendMovieQueries
    private val detailQuery = database.movieDetailTableQueries

    override fun clearAndInsertMovies(movies: List<MovieDto>) {
        trendingQuery.transaction {
            trendingQuery.deleteAll()
            movies.forEach { movie ->
                trendingQuery.insert(
                    id = movie.id,
                    title = movie.title,
                    image = W3WTheMovieConst.BASE_IMAGE_URL + movie.posterPath,
                    year = movie.releaseDate,
                    rating = movie.voteAverage,
                )
            }
        }
    }

    override fun getAllTrendMovie(): List<TrendMovie> = trendingQuery.selectAll().executeAsList()

    override fun insertOrUpdateDetail(detail: MovieDetailTable) {
        detailQuery.insert(detail)
    }

    override fun getDetail(id: Long): MovieDetailTable? = detailQuery.selectById(id).executeAsOneOrNull()
}
