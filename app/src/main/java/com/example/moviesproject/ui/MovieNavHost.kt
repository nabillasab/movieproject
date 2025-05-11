package com.example.moviesproject.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.moviesproject.data.MovieRepositoryImpl
import com.example.moviesproject.domain.GetMovieDetailUseCase
import com.example.moviesproject.domain.GetMovieListUseCase
import com.example.moviesproject.ui.tmdb.MovieDetailScreen
import com.example.moviesproject.ui.tmdb.MovieDetailViewModel
import com.example.moviesproject.ui.tmdb.MovieListScreen
import com.example.moviesproject.ui.tmdb.MovieListViewModel

@Composable
fun MovieNavHost(
    navHostController: NavHostController,
    modifier: Modifier = Modifier
) {
    val repository = MovieRepositoryImpl()
    val getMovieListUseCase = GetMovieListUseCase(repository)
    val getMovieDetailUseCase = GetMovieDetailUseCase(repository)

    NavHost(
        navController = navHostController,
        startDestination = "movie_list",
        modifier = modifier
    ) {
        composable("movie_list") {
            MovieListScreen(
                viewModel(factory = MovieListViewModel.provideFactory(getMovieListUseCase)),
                onMovieClick = { movieId ->
                    navHostController.navigate("detail/$movieId")
                })
        }
        composable(
            route = "detail/{movieId}"
        ) { navBackStackEntry ->
            val movieId = navBackStackEntry.arguments?.getString("movieId") ?: ""

            MovieDetailScreen(
                viewModel(
                    factory = MovieDetailViewModel.provideFactory(
                        movieId, getMovieDetailUseCase
                    )
                )
            )
        }
    }
}