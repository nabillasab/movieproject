package com.example.moviesproject.data.network

import com.example.moviesproject.BuildConfig
import com.example.moviesproject.data.MovieData
import com.example.moviesproject.data.MovieResponse
import javax.inject.Inject

class MovieNetworkDataSource @Inject constructor(
    val movieTmdbApi: MovieTmdbApi
) : NetworkDataSource {

    override suspend fun getMovieList(): MovieResponse {
        val queryMap = mutableMapOf<String, String>()
        queryMap["language"] = "en-US"
        queryMap["page"] = "1"

        val headers = mutableMapOf<String, String>()
        headers["accept"] = "application/json"
        headers["Authorization"] = "Bearer $TOKEN"

        return movieTmdbApi.getMoviesNowPlaying(queryMap, headers)
    }

    override suspend fun getMovieDetail(movieId: String): MovieData {
        val queryMap = mutableMapOf<String, String>()
        queryMap["language"] = "en-US"

        val headers = mutableMapOf<String, String>()
        headers["accept"] = "application/json"
        headers["Authorization"] = "Bearer $TOKEN"

        return movieTmdbApi.getMovieDetail(movieId, queryMap, headers)
    }

    companion object {
        private const val TOKEN = BuildConfig.API_KEY
    }
}