package com.example.moviesproject.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MovieDao {

    @Query("SELECT * FROM movies")
    suspend fun getAllOnce(): List<MovieEntity>

    @Query("SELECT * FROM movies WHERE id == :movieId")
    suspend fun getMovieDetail(movieId: String): MovieEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<MovieEntity>)
}