package com.yousuf.photos.ui.screen

import androidx.compose.runtime.Composable
import com.yousuf.photos.data.PhotoDetails

@Composable
fun AsyncPhotoImage(
    isThumbnail: Boolean,
    photoDetails: PhotoDetails,
) {
    LocalAsyncImage(photoDetails, isThumbnail)
}