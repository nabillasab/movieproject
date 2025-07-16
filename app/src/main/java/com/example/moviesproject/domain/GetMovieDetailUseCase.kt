package com.example.moviesproject.domain

import com.example.moviesproject.Movie
import com.example.moviesproject.data.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMovieDetailUseCase @Inject constructor(
    private val repository: MovieRepository
) {

    fun execute(movieId: String): Flow<Result<Movie>> {
        return repository.getMovieDetail(movieId)
    }
}