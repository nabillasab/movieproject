package com.example.moviesproject.di

import com.example.moviesproject.data.MovieRepositoryImpl
import com.example.moviesproject.data.network.MovieNetworkDataSource
import com.example.moviesproject.data.network.MovieTmdbApi
import com.example.moviesproject.data.network.NetworkDataSource
import com.example.moviesproject.data.network.NetworkExceptionInterceptor
import com.example.moviesproject.domain.MovieRepository
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MovieModules {

    @Singleton
    @Binds
    abstract fun bindMovieRepository(repository: MovieRepositoryImpl): MovieRepository

    @Singleton
    @Binds
    abstract fun bindNetworkDataSource(networkDataSource: MovieNetworkDataSource): NetworkDataSource

}

@Module
@InstallIn(SingletonComponent::class)
object NetworkModules {

    @Provides
    fun provideGson(): Gson = GsonBuilder().setLenient().create()

    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return logging
    }

    @Provides
    fun provideNetworkExceptionInterceptor(): NetworkExceptionInterceptor =
        NetworkExceptionInterceptor()

    @Provides
    fun provideOkhttpClient(
        networkExceptionInterceptor: NetworkExceptionInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(networkExceptionInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Provides
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    fun provideMovieTmdbApi(retrofit: Retrofit): MovieTmdbApi {
        return retrofit.create(MovieTmdbApi::class.java)
    }
}

