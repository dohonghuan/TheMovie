package com.example.w3wthemovie.android.ui.previewdata

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.w3wthemovie.common.model.Movie

class MoviesParameterProvider : PreviewParameterProvider<List<Movie>> {
    override val values: Sequence<List<Movie>> = sequenceOf(
        listOf(
            Movie(
                id = 1,
                title = "Forrest Gump",
                image = "",
                voteAverage = 10.0,
                releaseDate = "2025-01-01",
            ),
            Movie(
                id = 2,
                title = "Doraemon",
                image = "",
                voteAverage = 8.123,
                releaseDate = "2025-12-11",
            )
        )
    )
}