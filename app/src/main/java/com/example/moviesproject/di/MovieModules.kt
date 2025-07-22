package com.example.moviesproject.di

import android.content.Context
import androidx.room.Room
import com.example.moviesproject.data.MovieRepositoryImpl
import com.example.moviesproject.data.factory.ApiClientFactory
import com.example.moviesproject.data.factory.MockClientFactory
import com.example.moviesproject.data.factory.MockMovieDetailService
import com.example.moviesproject.data.factory.MockMovieListService
import com.example.moviesproject.data.factory.MovieDetailService
import com.example.moviesproject.data.factory.MovieListService
import com.example.moviesproject.data.factory.NetworkClientFactory
import com.example.moviesproject.data.factory.NetworkMovieDetailService
import com.example.moviesproject.data.factory.NetworkMovieListService
import com.example.moviesproject.data.local.MovieDao
import com.example.moviesproject.data.local.MovieDatabase
import com.example.moviesproject.data.network.MovieNetworkDataSource
import com.example.moviesproject.data.network.MovieTmdbApi
import com.example.moviesproject.data.network.NetworkDataSource
import com.example.moviesproject.data.network.NetworkExceptionInterceptor
import com.example.moviesproject.domain.MovieRepository
import com.example.moviesproject.util.image.CoilImageLoader
import com.example.moviesproject.util.image.ComposeImageLoader
import com.example.moviesproject.util.image.GlideImageLoader
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
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
object ImageLoaderModules {

    @Provides
    @Singleton
    @Named("coil")
    fun provideCoilImageLoader(coilImageLoader: CoilImageLoader): ComposeImageLoader =
        CoilImageLoader()

    @Provides
    @Singleton
    @Named("glide")
    fun provideGlideImageLoader(
        glideImageLoader: GlideImageLoader,
        @ApplicationContext context: Context
    ): ComposeImageLoader =
        GlideImageLoader(context)
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

    @Singleton
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

    @Singleton
    @Provides
    @Named("mock")
    fun provideMockMovieListService(): MovieListService = MockMovieListService()

    @Singleton
    @Provides
    @Named("mock")
    fun provideMockMovieDetailService(): MovieDetailService = MockMovieDetailService()

    @Singleton
    @Provides
    @Named("network")
    fun provideNetworkMovieListService(
        networkDataSource: MovieNetworkDataSource,
        dao: MovieDao, @IoDispatcher dispatcher: CoroutineDispatcher
    ): MovieListService = NetworkMovieListService(networkDataSource, dao, dispatcher)

    @Singleton
    @Provides
    @Named("network")
    fun provideNetworkMovieDetailService(
        networkDataSource: MovieNetworkDataSource,
        dao: MovieDao, @IoDispatcher dispatcher: CoroutineDispatcher
    ): MovieDetailService = NetworkMovieDetailService(networkDataSource, dao, dispatcher)

    //WE CAN SWITCH TO USE MOCK / NETWORK DATA HERE
    @Singleton
    @Provides
    fun provideApiFactory(
        mockClientFactory: MockClientFactory,
        networkClientFactory: NetworkClientFactory
    ): ApiClientFactory {
        return networkClientFactory
    }

}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModules {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): MovieDatabase {
        return Room.databaseBuilder(
            context, MovieDatabase::class.java, "movie_db"
        ).build()
    }

    @Provides
    fun provideMovieDao(database: MovieDatabase): MovieDao {
        return database.movieDao()
    }
}
