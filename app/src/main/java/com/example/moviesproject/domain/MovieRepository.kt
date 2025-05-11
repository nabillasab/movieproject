package com.example.moviesproject.domain

import com.example.moviesproject.ui.tmdb.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    fun getMovieList(): Flow<List<Movie>>

    fun getMovieDetail(movieId: Int): Flow<Movie>
}