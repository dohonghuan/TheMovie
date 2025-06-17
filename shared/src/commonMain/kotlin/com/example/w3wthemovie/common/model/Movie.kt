package com.example.w3wthemovie.common.model

import com.example.w3wthemovie.TrendMovie
import com.example.w3wthemovie.common.W3WTheMovieConst
import com.example.w3wthemovie.data.network.model.MovieDto

data class Movie(
    val id: Long,
    val image: String?,
    val title: String,
    val releaseDate: String?,
    val voteAverage: Double?,
)

fun List<MovieDto>.dtoMapToMovies(): List<Movie> {
    return this.map {
        Movie(
            id = it.id,
            image = if (it.posterPath != null) {
                W3WTheMovieConst.BASE_IMAGE_URL + it.posterPath
            } else {
                null
            },
            title = it.title,
            releaseDate = it.releaseDate,
            voteAverage = it.voteAverage,
        )
    }
}

fun List<TrendMovie>.entityMapToMovies(): List<Movie> {
    return this.map {
        Movie(
            id = it.id,
            image = it.image,
            title = it.title,
            releaseDate = it.year,
            voteAverage = it.rating,
        )
    }
}
