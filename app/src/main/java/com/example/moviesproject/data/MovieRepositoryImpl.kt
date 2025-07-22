package com.example.moviesproject.data

import com.example.moviesproject.Movie
import com.example.moviesproject.data.factory.ApiClientFactory
import com.example.moviesproject.domain.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepositoryImpl @Inject constructor(
    private val factory: ApiClientFactory
) : MovieRepository {

    override fun getMovieList(): Flow<Result<List<Movie>>> {
        return factory.createMovieListService().getMovieList()
    }

    override fun getMovieDetail(movieId: String): Flow<Result<Movie>> {
        return factory.createMovieDetailService().getMovieDetail(movieId)
    }
}