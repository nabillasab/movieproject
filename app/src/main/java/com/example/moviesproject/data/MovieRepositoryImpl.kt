package com.example.moviesproject.data

import com.example.moviesproject.Movie
import com.example.moviesproject.data.network.NetworkDataSource
import com.example.moviesproject.di.IoDispatcher
import com.example.moviesproject.domain.MovieRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepositoryImpl @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : MovieRepository {

    override fun getMovieList(): Flow<Result<List<Movie>>> {
        return flow {
            emit(Result.Loading)
            try {
                val response = networkDataSource.getMovieList()
                emit(Result.Success(movieMapper(response.results)))
            } catch (exception: Exception) {
                emit(Result.Error(exception.message ?: "unexpected error"))
            }
        }.flowOn(dispatcher)
    }

    override fun getMovieDetail(movieId: String): Flow<Result<Movie>> {
        return flow {
            emit(Result.Loading)
            try {
                val response = networkDataSource.getMovieDetail(movieId.toString())
                emit(Result.Success(movieDetailMapper(response)))
            } catch (exception: Exception) {
                emit(Result.Error(exception.message ?: "unexpected error"))
            }
        }.flowOn(dispatcher)
    }
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

private fun movieDetailMapper(movieData: MovieData): Movie {
    return Movie(
        id = movieData.id,
        title = movieData.title,
        originalLanguage = movieData.originalLanguage,
        overview = movieData.overview,
        posterPath = movieData.posterPath,
        releaseDate = movieData.releaseDate,
        voteAverage = movieData.voteAverage,
        voteCount = movieData.voteCount
    )
}

/*
private fun getData(): List<Movie> {
    val gson = Gson()
    val movieResponse: MovieResponse =
        gson.fromJson(MockData.getMovieList(), MovieResponse::class.java)
    return movieMapper(movieResponse.results)
}
 */