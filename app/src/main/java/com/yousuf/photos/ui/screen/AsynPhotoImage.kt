package com.yousuf.photos.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.yousuf.photos.R
import com.yousuf.photos.model.data.PhotoDetails
import com.yousuf.photos.viewmodel.BitmapState
import com.yousuf.photos.viewmodel.ImageLoaderViewModel

@SuppressLint("UnrememberedMutableState")
@Composable
fun AsyncPhotoImage(
    photoDetails: PhotoDetails,
    imageLoaderViewModel: ImageLoaderViewModel = hiltViewModel(key = "image-loader"),
) {

    val configuration = LocalConfiguration.current

    val bitmapState = mutableStateOf<BitmapState>(BitmapState.None)

    val rememberedBitmapState by remember { bitmapState }

    LaunchedEffect("image") {
        if (rememberedBitmapState is BitmapState.None) {
            imageLoaderViewModel.getBitmap(configuration, photoDetails, bitmapState)
        }
    }
    AsyncPhoto(rememberedBitmapState)
}

@Composable
fun AsyncPhoto(state: BitmapState) = Box(modifier=Modifier.fillMaxWidth(),
    contentAlignment = Alignment.Center,
) {
    when (state) {
        is BitmapState.None -> {}
        is BitmapState.Loading -> CircularProgressIndicator()
        is BitmapState.Error ->
            Image(
                painter = painterResource(id = R.drawable.img_outline),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxWidth()
            )

        is BitmapState.Success ->
            Image(
                painter = BitmapPainter(state.bitmap.asImageBitmap()),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth()
            )
    }
}