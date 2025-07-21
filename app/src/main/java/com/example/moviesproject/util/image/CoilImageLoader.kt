package com.example.moviesproject.util.image

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.moviesproject.R
import javax.inject.Inject

class CoilImageLoader @Inject constructor() : ComposeImageLoader {

    @Composable
    override fun LoadImage(url: String, modifier: Modifier) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(url)
                .placeholder(R.drawable.img_loading)
                .error(R.drawable.img_error)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier
        )
        Log.d("CoilImageLoader", "LoadImage: success")
    }
}