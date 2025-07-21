package com.example.moviesproject.util.image

import android.content.Context
import android.util.Log
import android.widget.ImageView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.bumptech.glide.Glide
import com.example.moviesproject.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GlideImageLoader @Inject constructor(
    @ApplicationContext val context: Context
) : ComposeImageLoader {

    @Composable
    override fun LoadImage(url: String, modifier: Modifier) {
        AndroidView(
            factory = {
                ImageView(it).apply {
                    scaleType = ImageView.ScaleType.CENTER_CROP
                }
            },
            update = { imageView ->
                Glide.with(context)
                    .load(url)
                    .placeholder(R.drawable.img_loading)
                    .into(imageView)
            },
            modifier = modifier
        )
        Log.d("GlideImageLoader", "LoadImage: success")
    }
}