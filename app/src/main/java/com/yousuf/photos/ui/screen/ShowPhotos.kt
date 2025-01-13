package com.yousuf.photos.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.yousuf.photos.network.data.PhotoDetails
import com.yousuf.photos.ui.nav.Destination
import com.yousuf.photos.viewmodel.PhotosViewModel

@Composable
fun ShowPhotos(
    navController: NavHostController,
    viewModel: PhotosViewModel = hiltViewModel(key = "photos"),
) {
    val photos by remember("photos") { viewModel.photosFlow }

    LazyColumn {
        photos.forEach {
            item {
                PhotoItem(photoDetails = it, navController)
            }
        }
    }
}

@Composable
fun PhotoItem(
    photoDetails: PhotoDetails,
    navController: NavHostController,
    photosViewModel: PhotosViewModel = hiltViewModel(key = "photos"),
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate("${Destination.Details.route}/${photoDetails.id}") {
                    launchSingleTop = true
                    restoreState = true
                }
            },
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .padding(4.dp)
                .height(52.dp)
                .width(52.dp),
            contentAlignment = Alignment.Center
        ) {
            AsyncPhotoImage(
                isThumbnail = true,
                photoDetails = photoDetails,
                imageLoaderType = photosViewModel.imageLoaderType,
                url = photoDetails.downloadUrl,
                modifier = Modifier
                    .height(48.dp)
                    .width(48.dp),
            )
        }

        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = photoDetails.filename,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

