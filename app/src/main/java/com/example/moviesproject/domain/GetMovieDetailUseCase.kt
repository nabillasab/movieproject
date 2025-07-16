package com.example.moviesproject.domain

import com.example.moviesproject.data.Movie
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMovieDetailUseCase @Inject constructor(
    private val repository: MovieRepository
) {

    fun execute(movieId: String): Flow<Movie> {
        return repository.getMovieDetail(movieId.toInt())
    }
}