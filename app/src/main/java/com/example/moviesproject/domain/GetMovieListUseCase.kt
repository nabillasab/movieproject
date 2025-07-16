package com.example.moviesproject.domain

import com.example.moviesproject.data.Movie
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMovieListUseCase @Inject constructor(
    private val repository: MovieRepository
) {

    fun execute(): Flow<List<Movie>> {
        return repository.getMovieList()
    }
}