package com.example.moviesproject.ui.tmdb

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.moviesproject.domain.GetMovieDetailUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MovieDetailViewModel(
    private val movieId: String,
    private val movieDetailUseCase: GetMovieDetailUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MovieDetailUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadMovieDetail()
    }

    private fun loadMovieDetail() {
        _uiState.update { _uiState.value.copy(isLoading = true) }

        viewModelScope.launch {
            movieDetailUseCase.execute(movieId)
                .catch { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = exception.message
                        )
                    }
                }
                .collect { movie ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            movie = movie,
                            errorMessage = null
                        )
                    }
                }
        }
    }

    companion object {
        fun provideFactory(
            movieId: String,
            movieDetailUseCase: GetMovieDetailUseCase
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {

            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(MovieDetailViewModel::class.java)) {
                    return MovieDetailViewModel(movieId, movieDetailUseCase) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}