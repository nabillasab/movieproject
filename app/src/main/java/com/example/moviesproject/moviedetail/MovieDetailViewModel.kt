package com.example.moviesproject.moviedetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesproject.Movie
import com.example.moviesproject.UiState
import com.example.moviesproject.data.Result
import com.example.moviesproject.domain.GetMovieDetailUseCase
import com.example.moviesproject.util.image.ComposeImageLoader
import com.example.moviesproject.util.image.ImageLoaderManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val movieDetailUseCase: GetMovieDetailUseCase,
    private val imageLoaderManager: ImageLoaderManager,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val movieId: String = savedStateHandle["movieId"] ?: ""
    private val _uiState = MutableStateFlow<UiState<Movie>>(UiState.Loading)
    val uiState: StateFlow<UiState<Movie>> = _uiState

    init {
        loadMovieDetail()
    }

    val imageLoader: ComposeImageLoader
        get() = imageLoaderManager.getCurrentImageLoader()

    private fun loadMovieDetail() {
        viewModelScope.launch {
            movieDetailUseCase.execute(movieId).collect { result ->
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