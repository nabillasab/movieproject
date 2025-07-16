package com.example.moviesproject.data.network

import com.example.moviesproject.data.MovieData
import com.example.moviesproject.data.MovieResponse
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface MovieTmdbApi {

    @GET("movie/now_playing")
    suspend fun getMoviesNowPlaying(
        @QueryMap queryParams: Map<String, String>,
        @HeaderMap headers: Map<String, String>
    ): MovieResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id") movieId: String,
        @QueryMap queryParams: Map<String, String>,
        @HeaderMap headers: Map<String, String>
    ): MovieData
}