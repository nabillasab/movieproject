package com.example.moviesproject.data.factory

import com.example.moviesproject.Movie
import kotlinx.coroutines.flow.Flow
import com.example.moviesproject.data.Result

interface ApiClientFactory {

    fun createMovieListService(): MovieListService

    fun createMovieDetailService(): MovieDetailService
}

interface MovieListService {
    fun getMovieList(): Flow<Result<List<Movie>>>
}

interface MovieDetailService {
    fun getMovieDetail(movieId: String): Flow<Result<Movie>>
}