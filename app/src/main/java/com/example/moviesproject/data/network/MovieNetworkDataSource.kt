package com.example.moviesproject.data.network

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

    companion object {
        val TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIzNzY3NWFlZDY3YWZmMDkwYmU2ZjFkNWUyN2IwZmFmMSIsIm5iZiI6MTc0NDgyMjkxNS43Nywic3ViIjoiNjdmZmUyODNkNjQ1ZTQxZTA5OTk3MDNkIiwic2NvcGVzIjpbImFwaV9yZWFkIl0sInZlcnNpb24iOjF9.habmUdNpVkndvVDaUYCVmLUkPEfzrlHCn5cW86Er1eY"
    }
}