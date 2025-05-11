package com.example.moviesproject.ui.tmdb

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.moviesproject.domain.GetMovieListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MovieListViewModel(
    private val movieListUseCase: GetMovieListUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MovieListUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadMovieList()
    }

    private fun loadMovieList() {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            movieListUseCase.execute()
                .catch { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = exception.message
                        )
                    }
                }
                .collect { movies ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            movieList = movies,
                            errorMessage = null
                        )
                    }
                }
        }
    }

    companion object {
        fun provideFactory(movieListUseCase: GetMovieListUseCase): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {

                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(MovieListViewModel::class.java)) {
                        return MovieListViewModel(movieListUseCase) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
    }
}