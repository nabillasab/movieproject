package com.example.moviesproject.domain

import com.example.moviesproject.data.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    fun getMovieList(): Flow<List<Movie>>

    fun getMovieDetail(movieId: Int): Flow<Movie>
}