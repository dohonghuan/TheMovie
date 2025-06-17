package com.example.w3wthemovie.common.model

import com.example.w3wthemovie.MovieDetailTable
import com.example.w3wthemovie.common.W3WTheMovieConst
import com.example.w3wthemovie.data.network.model.MovieDetailResponse

data class MovieDetail(
    val id: Long,
    val isAdult: Boolean,
    val backdrop: String?,
    val belongsToCollection: MovieCollection?,
    val budget: Long,
    val genres: String,
    val homepage: String,
    val overview: String,
    val popularity: Double,
    val posterPath: String?,
    val productionCompanies: String,
    val productionCountries: String,
    val releaseDate: String,
    val revenue: Double,
    val runtime: String,
    val spokenLanguages: String,
    val status: String,
    val tagline: String,
    val title: String,
    val voteAverage: Double,
    val voteCount: Long,
)

fun MovieDetailResponse.dtoMapToMovieDetail(): MovieDetail {
    val hours = this.runtime.div(60)
    val minutes = this.runtime.mod(60)
    val runtimeInString = if (hours > 0) {
        hours.toString() + "h "
    } else {
        ""
    } + if (minutes != 0) {
        minutes.toString() + "m"
    } else {
        ""
    }
    return MovieDetail(
        id = this.id,
        isAdult = this.isAdult,
        backdrop = if (this.backdropPath != null) {
            W3WTheMovieConst.BASE_IMAGE_URL + this.backdropPath
        } else {
            null
        },
        belongsToCollection = if (this.belongsToCollection == null) {
            null
        } else {
            MovieCollection(
                name = this.belongsToCollection.name,
                poster = if (this.belongsToCollection.backdropPath != null) {
                    W3WTheMovieConst.BASE_IMAGE_URL + this.belongsToCollection.posterPath
                } else {
                    null
                },
                backdrop = if (this.belongsToCollection.backdropPath != null) {
                    W3WTheMovieConst.BASE_IMAGE_URL + this.belongsToCollection.backdropPath
                } else {
                    null
                },
            )
        },
        budget = this.budget,
        genres = this.genres.joinToString(", ") { it.name },
        homepage = this.homepage,
        overview = this.overview,
        popularity = this.popularity,
        posterPath = if (this.posterPath != null) {
            W3WTheMovieConst.BASE_IMAGE_URL + this.posterPath
        } else {
            null
        },
        productionCompanies = this.productionCompanies.joinToString("\n") {
            " - ${it.name}"
        },
        productionCountries = this.productionCountries.joinToString("\n") {
            " - ${it.name}"
        },
        releaseDate = this.releaseDate,
        revenue = this.revenue,
        runtime = runtimeInString,
        spokenLanguages = this.spokenLanguages.joinToString(", ") { it.englishName },
        status = this.status,
        tagline = this.tagline,
        title = this.title,
        voteAverage = this.voteAverage,
        voteCount = this.voteCount,
    )
}

fun MovieDetail.mapToMovieDetailTable(syncTime: Long): MovieDetailTable = MovieDetailTable(
    id  = this.id,
    isAdult = this.isAdult,
    backdrop = this.backdrop,
    collectionName = this.belongsToCollection?.name,
    collectionPoster = this.belongsToCollection?.poster,
    collectionBackdrop = this.belongsToCollection?.backdrop,
    budget = this.budget,
    genres = this.genres,
    homepage = this.homepage,
    overview = this.overview,
    popularity = this.popularity,
    poster = this.posterPath,
    productionCompanies = this.productionCompanies,
    productionCountries = this.productionCountries,
    releaseDate = this.releaseDate,
    revenue = this.revenue,
    runtime = this.runtime,
    spokenLanguages = this.spokenLanguages,
    status = this.status,
    tagline = this.tagline,
    title = this.title,
    voteAverage = this.voteAverage,
    voteCount = this.voteCount,
    syncTime = syncTime,
)


fun MovieDetailTable.entityMapToMovieDetail(): MovieDetail {
    val collectionName = this.collectionName
    val collectionPoster = this.collectionPoster
    val collectionBackdrop = this.collectionBackdrop
    var collection: MovieCollection? = null
    if (collectionName != null || collectionBackdrop != null || collectionPoster != null) {
        collection = MovieCollection(
            name = collectionName,
            poster = collectionPoster,
            backdrop = collectionBackdrop,
        )
    }
    return MovieDetail(
        id  = this.id,
        isAdult = this.isAdult ?: false,
        backdrop = this.backdrop,
        belongsToCollection = collection,
        budget = this.budget,
        genres = this.genres,
        homepage = this.homepage,
        overview = this.overview,
        popularity = this.popularity,
        posterPath = this.poster,
        productionCompanies = this.productionCompanies,
        productionCountries = this.productionCountries,
        releaseDate = this.releaseDate,
        revenue = this.revenue,
        runtime = this.runtime,
        spokenLanguages = this.spokenLanguages,
        status = this.status,
        tagline = this.tagline,
        title = this.title,
        voteAverage = this.voteAverage,
        voteCount = this.voteCount,
    )
}
