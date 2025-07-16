package com.example.moviesproject.domain

import com.example.moviesproject.Movie
import com.example.moviesproject.data.Result
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    fun getMovieList(): Flow<Result<List<Movie>>>

    fun getMovieDetail(movieId: Int): Flow<Movie>
}