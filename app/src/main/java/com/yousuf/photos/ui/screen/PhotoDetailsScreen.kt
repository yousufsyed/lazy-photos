package com.yousuf.photos.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yousuf.photos.viewmodel.PhotosViewModel

@Composable
fun PhotoDetailsScreen(
    photoId: Int,
    photosViewModel: PhotosViewModel = hiltViewModel(key = "image-loader"),
) {
    LaunchedEffect(photoId) {
        photosViewModel.fetchPhotoDetails(photoId)
    }

    val photoDetails by remember { photosViewModel.photoDetails }

    photoDetails?.let { photo ->
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .fillMaxSize(.7f),
                contentAlignment = Alignment.BottomCenter
            ) {
                AsyncPhotoImage(
                    isThumbnail = false,
                    photoDetails = photo,
                )
            }

            Text(
                modifier = Modifier
                    .fillMaxHeight(.2f)
                    .padding(start = 8.dp, top = 8.dp),
                text = photo.author,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
            )
        }
    } ?: InvalidPhotoDetails()
}

@Composable
fun InvalidPhotoDetails() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Invalid Photo Details",
            fontSize = 14.sp,
            modifier = Modifier.padding(start = 8.dp),
        )
    }
}
