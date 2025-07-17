package com.example.moviesproject.data

import android.util.Log
import com.example.moviesproject.Movie
import com.example.moviesproject.data.local.MovieDao
import com.example.moviesproject.data.local.MovieEntity
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
    private val dao: MovieDao,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : MovieRepository {

    override fun getMovieList(): Flow<Result<List<Movie>>> {
        return flow {
            emit(Result.Loading)
            val moviesLocalData = dao.getAllOnce()
            try {
                if (isStale(
                        moviesLocalData.isEmpty(),
                        moviesLocalData.firstOrNull()?.lastUpdated ?: 0
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
                emit(Result.Error(exception.message ?: "unexpected error"))
                if (moviesLocalData.isNotEmpty()) emit(Result.Success(movieMapper(moviesLocalData)))
            }
        }.flowOn(dispatcher)
    }

    private fun isStale(isLocalDataEmpty: Boolean, lastUpdated: Long): Boolean {
        return isLocalDataEmpty || (System.currentTimeMillis() - lastUpdated > CACHE_TIMEOUT_MS)
    }

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

    private fun movieEntityMapper(movieDataList: List<MovieData>): List<MovieEntity> {
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

    private fun movieMapper(movieDataList: List<MovieEntity>): List<Movie> {
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

    private fun movieDetailEntityMapper(movieEntity: MovieEntity): Movie {
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

    companion object {
        private const val CACHE_TIMEOUT_MS = 20 * 60 * 1000L //20 mins
    }
}

/*
private fun getData(): List<Movie> {
    val gson = Gson()
    val movieResponse: MovieResponse =
        gson.fromJson(MockData.getMovieList(), MovieResponse::class.java)
    return movieMapper(movieResponse.results)
}
 */