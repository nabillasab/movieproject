package com.example.moviesproject.ui.tmdb

data class MovieListUiState(
    val isLoading: Boolean = false,
    val movieList: List<Movie> = emptyList(),
    val errorMessage: String? = null
)

data class MovieDetailUiState(
    val isLoading: Boolean = false,
    val movie: Movie? = null,
    val errorMessage: String? = null
)