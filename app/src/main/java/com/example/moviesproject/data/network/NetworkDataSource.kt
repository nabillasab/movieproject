package com.example.moviesproject.data.network

import com.example.moviesproject.data.MovieResponse

interface NetworkDataSource {

    suspend fun getMovieList(): MovieResponse
}