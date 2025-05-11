package com.example.moviesproject.domain

import com.example.moviesproject.ui.tmdb.Movie
import kotlinx.coroutines.flow.Flow

class GetMovieDetailUseCase(
    private val repository: MovieRepository
) {

    fun execute(movieId: String): Flow<Movie> {
        return repository.getMovieDetail(movieId.toInt())
    }
}