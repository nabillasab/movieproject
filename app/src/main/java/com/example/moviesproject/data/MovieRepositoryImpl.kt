package com.example.moviesproject.data

import com.example.moviesproject.domain.MovieRepository
import com.example.moviesproject.ui.tmdb.Movie
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MovieRepositoryImpl : MovieRepository {

    override fun getMovieList(): Flow<List<Movie>> {
        return flow {
            emit(getData())
        }
    }

    override fun getMovieDetail(movieId: Int): Flow<Movie> {
        return flow {
            getData().forEach { movie ->
                if (movie.id == movieId) {
                    emit(movie)
                    return@forEach
                }
            }
        }
    }
}

//TODO changes this with API
private fun getData(): List<Movie> {
    val gson = Gson()
    val movieResponse: MovieResponse =
        gson.fromJson(MockData.getMovieList(), MovieResponse::class.java)
    return movieMapper(movieResponse.results)
}

private fun movieMapper(movieDataList: List<MovieData>): List<Movie> {
    val movieList = mutableListOf<Movie>()
    movieDataList.forEach { movieData ->
        val movie = Movie(
            id = movieData.id,
            title = movieData.title,
            originalLanguage = movieData.originalLanguage,
            overview = movieData.overview,
            posterPath = movieData.posterPath,
            releaseDate = movieData.releaseDate,
            voteAverage = movieData.voteAverage,
            voteCount = movieData.voteCount
        )
        movieList.add(movie)
    }
    return movieList
}