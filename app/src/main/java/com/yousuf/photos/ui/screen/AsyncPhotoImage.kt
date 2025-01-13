package com.yousuf.photos.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.yousuf.photos.R
import com.yousuf.photos.model.data.ImageLoader
import com.yousuf.photos.network.data.PhotoDetails

@Composable
fun AsyncPhotoImage(
    isThumbnail: Boolean,
    url: String,
    modifier: Modifier = Modifier,
    photoDetails: PhotoDetails,
    imageLoaderType: ImageLoader = ImageLoader.LOCAL,
) {

    when (imageLoaderType) {
        ImageLoader.COIL ->
            AsyncImage(
                model = url,
                contentDescription = null,
                modifier = modifier,
                alignment = if (isThumbnail) Alignment.Center else Alignment.BottomCenter,
                error = painterResource(R.drawable.img_outline)
            )

        ImageLoader.LOCAL -> LocalAsyncImage(photoDetails, isThumbnail)
    }
}