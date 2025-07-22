package com.example.moviesproject.data.factory

import android.util.Log
import com.example.moviesproject.Movie
import com.example.moviesproject.data.MovieData
import com.example.moviesproject.data.Result
import com.example.moviesproject.data.local.MovieDao
import com.example.moviesproject.data.local.MovieEntity
import com.example.moviesproject.data.network.NetworkDataSource
import com.example.moviesproject.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Named

class NetworkClientFactory @Inject constructor(
    @Named("network") private val networkMovieListService: MovieListService,
    @Named("network") private val networkMovieDetailService: MovieDetailService
) : ApiClientFactory {

    override fun createMovieListService(): MovieListService {
        return networkMovieListService
    }

    override fun createMovieDetailService(): MovieDetailService {
        return networkMovieDetailService
    }
}

class NetworkMovieListService @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val dao: MovieDao,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : MovieListService {

    override fun getMovieList(): Flow<Result<List<Movie>>> {
        return flow {
            emit(Result.Loading)
            val moviesLocalData = dao.getAllOnce()
            try {
                if (isStale(
                        moviesLocalData.isEmpty(), moviesLocalData.firstOrNull()?.lastUpdated ?: 0
                    )
                ) {
                    val response = networkDataSource.getMovieList()
                    val movieListEntity = movieEntityMapper(response.results)
                    Log.d("repository", "getMovieList: network")
                    dao.insertAll(movieListEntity)
                    emit(Result.Success(movieMapper(movieListEntity)))
                } else {
                    Log.d("repository", "getMovieList: local")
                    emit(Result.Success(movieMapper(moviesLocalData)))
                }
            } catch (exception: Exception) {
                emit(
                    Result.Error(
                        exception.message ?: "unexpected error"
                    )
                )
                if (moviesLocalData.isNotEmpty()) emit(
                    Result.Success(
                        movieMapper(
                            moviesLocalData
                        )
                    )
                )
            }
        }.flowOn(dispatcher)
    }

    private fun isStale(isLocalDataEmpty: Boolean, lastUpdated: Long): Boolean {
        return isLocalDataEmpty || (System.currentTimeMillis() - lastUpdated > CACHE_TIMEOUT_MS)
    }

    companion object {
        private const val CACHE_TIMEOUT_MS = 20 * 60 * 1000L //20 mins
    }

    fun movieEntityMapper(movieDataList: List<MovieData>): List<MovieEntity> {
        return movieDataList.map {
            MovieEntity(
                id = it.id,
                title = it.title,
                originalLanguage = it.originalLanguage,
                overview = it.overview,
                posterPath = it.posterPath,
                releaseDate = it.releaseDate,
                voteAverage = it.voteAverage,
                voteCount = it.voteCount,
                lastUpdated = System.currentTimeMillis()
            )
        }
    }

    fun movieMapper(movieDataList: List<MovieEntity>): List<Movie> {
        return movieDataList.map {
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

class NetworkMovieDetailService @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val dao: MovieDao,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : MovieDetailService {

    override fun getMovieDetail(movieId: String): Flow<Result<Movie>> {
        return flow {
            emit(Result.Loading)

            try {
                val movieDetailLocal = dao.getMovieDetail(movieId)
                if (movieDetailLocal == null) {
                    val response = networkDataSource.getMovieDetail(movieId.toString())
                    emit(Result.Success(movieDetailMapper(response)))
                    Log.d("repository", "getMovieDetail: network")
                } else {
                    Log.d("repository", "getMovieDetail: local")
                    emit(Result.Success(movieDetailEntityMapper(movieDetailLocal)))
                }

            } catch (exception: Exception) {
                emit(Result.Error(exception.message ?: "unexpected error"))
            }
        }.flowOn(dispatcher)
    }

    fun movieDetailEntityMapper(movieEntity: MovieEntity): Movie {
        return Movie(
            id = movieEntity.id,
            title = movieEntity.title,
            originalLanguage = movieEntity.originalLanguage,
            overview = movieEntity.overview,
            posterPath = movieEntity.posterPath,
            releaseDate = movieEntity.releaseDate,
            voteAverage = movieEntity.voteAverage,
            voteCount = movieEntity.voteCount
        )
    }

    fun movieDetailMapper(movieData: MovieData): Movie {
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
}