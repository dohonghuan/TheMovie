package com.example.w3wthemovie.android.ui.detail

import com.example.w3wthemovie.common.model.MovieDetail

sealed interface DetailUiState {
    data object Loading : DetailUiState

    data class Success(
        val detail: MovieDetail,
    ) : DetailUiState

    data class Error(val message: String) : DetailUiState
}
