package com.example.w3wthemovie.android.ui.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.w3wthemovie.android.ui.CommonResult
import com.example.w3wthemovie.common.extractMessage
import com.example.w3wthemovie.common.model.Movie
import com.example.w3wthemovie.data.repository.TheMovieRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class ListViewModel(
    private val theMovieRepository: TheMovieRepository,
    private val savedStateHandle: SavedStateHandle,
): ViewModel() {

    val searchValue = savedStateHandle.getStateFlow(key = SEARCH_VALUE, initialValue = "")

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val movies: StateFlow<CommonResult<List<Movie>>> =
        searchValue.debounce(1000L)
            .flatMapLatest { searchValue ->
                flow {
                    if (searchValue.isEmpty()) {
                        emit(CommonResult.Loading())
                    } else {
                        emit(CommonResult.Loading())
                        emit(
                            try {
                                CommonResult.Success(theMovieRepository.searchMovies(searchValue))
                            } catch (e: Exception) {
                                CommonResult.Error(extractMessage(e))
                            }
                        )
                    }
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = CommonResult.Loading(),
            )

    val trendMovies: StateFlow<CommonResult<List<Movie>>> =
        flow {
            try {
                val response = theMovieRepository.fetchTrendingMovies()
                emit(CommonResult.Success(response))
            } catch (e: Exception) {
                emit(CommonResult.Error(extractMessage(e)))
            }
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = CommonResult.Loading(),
            )

    fun onSearchValueChange(searchValue: String) {
        savedStateHandle[SEARCH_VALUE] = searchValue
    }

    companion object {
        private const val SEARCH_VALUE = "search_value"
        fun provideFactory(
            theMovieRepository: TheMovieRepository,
        ): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    ListViewModel(
                        theMovieRepository = theMovieRepository,
                        savedStateHandle = createSavedStateHandle(),
                    )
                }
            }
    }
}
