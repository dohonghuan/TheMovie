package com.example.w3wthemovie.android.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.toRoute
import com.example.w3wthemovie.android.ui.DetailRoute
import com.example.w3wthemovie.common.extractMessage
import com.example.w3wthemovie.data.repository.TheMovieRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class DetailViewModel(
    private val theMovieRepository: TheMovieRepository,
    savedStateHandle: SavedStateHandle,
): ViewModel() {
    private val movieId: Long = (savedStateHandle.toRoute() as DetailRoute).id

    @OptIn(ExperimentalCoroutinesApi::class)
    val movieDetail: StateFlow<DetailUiState> = savedStateHandle.getStateFlow(
        key = MOVIE_ID_KEY,
        initialValue = movieId,
    ).flatMapLatest { id ->
        flow {
            emit(
                try {
                    val result = theMovieRepository.fetchMovieDetail(id)
                    DetailUiState.Success(result)
                } catch (e: Exception) {
                    DetailUiState.Error(extractMessage(e))
                }
            )
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = DetailUiState.Loading,
        )

    companion object {
        private const val MOVIE_ID_KEY = "movie_id_key"

        fun provideFactory(
            theMovieRepository: TheMovieRepository,
        ): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    DetailViewModel(
                        theMovieRepository = theMovieRepository,
                        savedStateHandle = createSavedStateHandle(),
                    )
                }
            }
    }
}