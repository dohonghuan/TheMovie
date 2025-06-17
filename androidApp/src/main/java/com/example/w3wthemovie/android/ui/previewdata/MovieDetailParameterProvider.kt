package com.example.w3wthemovie.android.ui.previewdata

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.w3wthemovie.common.model.MovieCollection
import com.example.w3wthemovie.common.model.MovieDetail

class MovieDetailParameterProvider: PreviewParameterProvider<MovieDetail> {
    override val values: Sequence<MovieDetail> = sequenceOf(
        MovieDetail(
            id = 1L,
            isAdult = true,
            backdrop = "",
            posterPath = "",
            belongsToCollection = MovieCollection(
                name = "How to Train Your Dragon (Live-Action) Collection",
                poster = "",
                backdrop = "",
            ),
            budget = 150000000L,
            genres = "Action, Family, Fantasy",
            homepage = "https://www.welcometoberk.com/",
            overview = "On the rugged isle of Berk, where Vikings and dragons have been bitter enemies for generations, Hiccup stands apart, defying centuries of tradition when he befriends Toothless, a feared Night Fury dragon. Their unlikely bond reveals the true nature of dragons, challenging the very foundations of Viking society.",
            popularity = 240.3715,
            productionCompanies =
                "- DreamWorks Animation\n" +
                        "- Marc Platt Productions",
            productionCountries = "- Vietnam\n" + "- United Kingdom\n" + "- Mongolia",
            releaseDate = "2025-06-06",
            revenue = 15700000.0,
            runtime = "1h 25m",
            spokenLanguages = "English, Vietnamese",
            status = "Released",
            tagline = "The legend is real.",
            title = "How to Train Your Dragon",
            voteAverage = 7.8,
            voteCount = 137,
        ),
    )
}