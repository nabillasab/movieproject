package com.example.moviesproject.util.image

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

interface ComposeImageLoader {

    @Composable
    fun LoadImage(url: String, modifier: Modifier)
}