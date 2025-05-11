package com.example.moviesproject.domain

import com.example.moviesproject.ui.tmdb.Movie
import kotlinx.coroutines.flow.Flow

class GetMovieListUseCase(private val repository: MovieRepository) {

    fun execute(): Flow<List<Movie>> {
        return repository.getMovieList()
    }
}