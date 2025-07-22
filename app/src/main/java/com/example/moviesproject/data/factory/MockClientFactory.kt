package com.example.moviesproject.data.factory

import com.example.moviesproject.Movie
import com.example.moviesproject.data.MovieData
import com.example.moviesproject.data.MovieResponse
import com.example.moviesproject.data.Result
import com.example.moviesproject.data.mock.MockData
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Named

class MockClientFactory @Inject constructor(
    @Named("mock") private val mockMovieListService: MovieListService,
    @Named("mock") private val mockMovieDetailService: MovieDetailService
) : ApiClientFactory {

    override fun createMovieListService(): MovieListService {
        return mockMovieListService
    }

    override fun createMovieDetailService(): MovieDetailService {
        return mockMovieDetailService
    }
}

class MockMovieListService : MovieListService {
    override fun getMovieList(): Flow<Result<List<Movie>>> {
        return flow {
            emit(Result.Success(MovieMock.getData()))
        }
    }
}

class MockMovieDetailService : MovieDetailService {
    override fun getMovieDetail(movieId: String): Flow<Result<Movie>> {
        return flow {
            MovieMock.getData().forEach { movie ->
                if (movie.id == movieId.toInt()) {
                    emit(Result.Success(movie))
                    return@forEach
                }
            }
        }
    }
}

object MovieMock {
    fun getData(): List<Movie> {
        val gson = Gson()
        val movieResponse: MovieResponse =
            gson.fromJson(MockData.getMovieList(), MovieResponse::class.java)
        return movieListMapper(movieResponse.results)
    }

    fun movieListMapper(movieData: List<MovieData>): List<Movie> {
        return movieData.map {
            Movie(
                id = it.id,
                title = it.title,
                originalLanguage = it.originalLanguage,
                overview = it.overview,
                posterPath = it.posterPath,
                releaseDate = it.releaseDate,
                voteAverage = it.voteAverage,
                voteCount = it.voteCount
            )
        }
    }
}