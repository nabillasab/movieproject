package com.example.moviesproject.data.network

interface NetworkDataSource {

    suspend fun getMovieList()
}