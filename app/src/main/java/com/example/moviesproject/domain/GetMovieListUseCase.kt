package com.example.moviesproject.domain

import com.example.moviesproject.Movie
import com.example.moviesproject.data.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMovieListUseCase @Inject constructor(
    private val repository: MovieRepository
) {

    fun execute(): Flow<Result<List<Movie>>> {
        return repository.getMovieList()
    }
}