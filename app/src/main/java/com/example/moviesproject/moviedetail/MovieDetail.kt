package com.example.moviesproject.moviedetail

import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.moviesproject.movielist.MoviePoster
import com.example.moviesproject.movielist.MovieRating
import com.example.moviesproject.movielist.MovieReleaseDate
import com.example.moviesproject.theme.MoviesProjectTheme
import com.example.moviesproject.data.Movie

@Composable
fun MovieDetailScreen(viewModel: MovieDetailViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    when {
        uiState.isLoading -> {

        }
        uiState.errorMessage != null -> {

        }
        else -> {
            uiState.movie?. let { movie ->
                MovieDetailContent(movie)
            }
        }
    }
}

@Composable
fun MovieDetailContent(movie: Movie, modifier: Modifier = Modifier) {
    Column {
        Column(
            modifier = modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MoviePoster(movie.posterPath, 180.dp, 270.dp)
        }
        MovieTitleDetail(movie.title)
        Row {
            MovieRating(movie.voteAverage, movie.voteCount)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                MovieReleaseDate(movie.releaseDate)
            }
        }
        AdditionalDetail(movie.originalLanguage)
        HorizontalDivider(modifier = modifier.padding(vertical = 16.dp))
        OverviewMovie(movie.overview)
    }
}

@Composable
fun AdditionalDetail(oriLanguange: String, modifier: Modifier = Modifier) {
    Text(
        text = "Language: $oriLanguange",
        modifier = modifier.padding(top = 8.dp)
    )
}

@Composable
fun OverviewMovie(overview: String) {
    Text(
        text = overview,
        textAlign = TextAlign.Justify
    )
}

@Composable
fun MovieTitleDetail(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
            .padding(top = 16.dp, bottom = 8.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun MoviesDetailPreview() {
    MoviesProjectTheme {
        val movie = Movie(
            762509,
            "Mufasa: The Lion King",
            "en",
            "Mufasa, a cub lost and alone, meets a sympathetic lion named Taka, the heir to a royal bloodline. The chance meeting sets in motion an expansive journey of a group of misfits searching for their destiny.",
            "/lurEK87kukWNaHd0zYnsi3yzJrs.jpg",
            "2024-12-18",
            7.417,
            2006
        )
        MovieDetailContent(movie)
    }
}