package com.example.moviesproject.movielist

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.moviesproject.Movie
import com.example.moviesproject.UiState
import com.example.moviesproject.util.image.CoilImageLoader
import com.example.moviesproject.util.image.ComposeImageLoader
import com.example.moviesproject.util.theme.MoviesProjectTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale


@Composable
fun MovieListScreen(
    onMovieClick: (Int) -> Unit,
    viewModel: MovieListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val imageLoader = viewModel.imageLoader

    when (uiState) {
        is UiState.Loading -> {}
        is UiState.Success -> {
            MovieListContent((uiState as UiState.Success<List<Movie>>).data,
                imageLoader, onMovieClick)
        }

        is UiState.Error -> {}
    }
}

@Composable
private fun MovieListContent(
    movieList: List<Movie>, imageLoader: ComposeImageLoader, onMovieClick: (Int) -> Unit
) {
    LazyColumn {
        items(movieList) { movie ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onMovieClick(movie.id) }
            ) {
                MovieItem(movie, imageLoader)
                ItemListDivider()
            }
        }
    }
}

@Composable
private fun ItemListDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = 8.dp),
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
    )
}

@Composable
fun MovieItem(movie: Movie, imageLoader: ComposeImageLoader, modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth()) {
        MoviePoster(movie.posterPath, 60.dp, 90.dp, imageLoader, modifier = modifier.padding(16.dp))
        Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.Center) {
            MovieTitle(movie.title)
            MovieRating(movie.voteAverage, movie.voteCount)
            Spacer(modifier = modifier.height(8.dp))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                MovieReleaseDate(movie.releaseDate)
            }
        }
    }
}

@Composable
fun MoviePoster(
    posterPath: String,
    width: Dp,
    height: Dp,
    imageLoader: ComposeImageLoader,
    modifier: Modifier = Modifier
) {
    val imgUrl = "https://image.tmdb.org/t/p/w500$posterPath"
    val modifier = modifier.width(width).height(height)
    imageLoader.LoadImage(imgUrl, modifier)
}

@Composable
fun MovieTitle(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
            .padding(end = 16.dp, top = 16.dp, bottom = 8.dp)
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MovieReleaseDate(releaseDate: String, modifier: Modifier = Modifier) {
    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val outputFormatter = DateTimeFormatter.ofPattern("yyyy", Locale.getDefault())
    val dateInput = LocalDate.parse(releaseDate, inputFormatter)
    val dateOutput = dateInput.format(outputFormatter)

    Text(
        text = dateOutput,
        fontSize = 16.sp
    )
}

@SuppressLint("DefaultLocale")
@Composable
fun MovieRating(rating: Double, total: Int, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(end = 16.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            tint = Color(0xFFFFC107), // gold star
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = modifier.width(5.dp))
        Text(
            text = String.format("%.1f", rating),
            fontSize = 16.sp
        )
        Spacer(modifier = modifier.width(5.dp))
        Text(
            text = "($total ratings)",
            fontSize = 16.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MoviesPreview() {
    MoviesProjectTheme {
        val movieList = mutableListOf<Movie>()
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
        movieList.add(movie)
        movieList.add(movie)
        MovieListContent(movieList, CoilImageLoader(), onMovieClick = {})
    }
}