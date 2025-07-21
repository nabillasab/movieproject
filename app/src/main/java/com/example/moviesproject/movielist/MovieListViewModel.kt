package com.example.moviesproject.movielist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesproject.Movie
import com.example.moviesproject.UiState
import com.example.moviesproject.data.Result
import com.example.moviesproject.domain.GetMovieListUseCase
import com.example.moviesproject.util.image.ComposeImageLoader
import com.example.moviesproject.util.image.ImageLoaderManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val movieListUseCase: GetMovieListUseCase,
    private val imageLoaderManager: ImageLoaderManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Movie>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Movie>>> = _uiState

    init {
        loadMovieList()
    }

    val imageLoader: ComposeImageLoader
        get() = imageLoaderManager.getCurrentImageLoader()

    private fun loadMovieList() {
        viewModelScope.launch {
            movieListUseCase.execute().collect { result ->
                val state = when (result) {
                    is Result.Loading -> UiState.Loading
                    is Result.Success -> UiState.Success(result.data)
                    is Result.Error -> UiState.Error(result.message)
                }
                _uiState.value = state
            }
        }
    }
}