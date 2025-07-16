package com.example.moviesproject.movielist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesproject.data.Movie
import com.example.moviesproject.domain.GetMovieListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MovieListUiState(
    val isLoading: Boolean = false,
    val movieList: List<Movie> = emptyList(),
    val errorMessage: String? = null
)

@HiltViewModel
class MovieListViewModel @Inject constructor(
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
}