package com.yousuf.photos.ui.screen

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.hilt.navigation.compose.hiltViewModel
import com.yousuf.photos.model.data.PhotoDetails
import com.yousuf.photos.viewmodel.BitmapState
import com.yousuf.photos.viewmodel.ImageLoaderViewModel

@SuppressLint("UnrememberedMutableState")
@Composable
fun AsyncThumbnail(
    photoDetails: PhotoDetails,
    imageLoaderViewModel: ImageLoaderViewModel = hiltViewModel(key = "image-loader"),
) {

    val configuration = LocalConfiguration.current

    val bitmapState = mutableStateOf<BitmapState>(BitmapState.None)

    val rememberedBitmapState by remember { bitmapState }

    LaunchedEffect("image") {
        if (rememberedBitmapState is BitmapState.None) {
            imageLoaderViewModel.getThumbnailBitmap(
                configuration, 48, photoDetails, bitmapState
            )
        }
    }

    AsyncPhoto(rememberedBitmapState)
}