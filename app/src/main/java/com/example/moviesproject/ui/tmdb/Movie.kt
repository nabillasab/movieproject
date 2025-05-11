package com.example.moviesproject.ui.tmdb

data class Movie(
    val id: Int,
    val title: String,
    val originalLanguage: String,
    val overview: String,
    val posterPath: String,
    val releaseDate: String,
    val voteAverage: Double,
    val voteCount: Int)