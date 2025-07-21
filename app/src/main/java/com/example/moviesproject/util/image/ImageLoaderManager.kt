package com.example.moviesproject.util.image

import javax.inject.Inject
import javax.inject.Named

class ImageLoaderManager @Inject constructor(
    @Named("coil") private val coilImageLoader: ComposeImageLoader,
    @Named("glide") private val glideImageLoader: ComposeImageLoader
) {

    val coilPreference: Boolean = false

    fun getCurrentImageLoader(): ComposeImageLoader {
        return if (coilPreference) coilImageLoader else glideImageLoader
    }
}