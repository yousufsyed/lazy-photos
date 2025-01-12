package com.yousuf.photos.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.yousuf.photos.R
import com.yousuf.photos.viewmodel.PhotosViewModel
import com.yousuf.photos.viewmodel.PhotosViewModel.UiState.Empty
import com.yousuf.photos.viewmodel.PhotosViewModel.UiState.Error
import com.yousuf.photos.viewmodel.PhotosViewModel.UiState.Success

@Composable
fun PhotoListScreen(
    navController: NavHostController,
    viewModel: PhotosViewModel = hiltViewModel()
) {
    val uiState = rememberSaveable("PhotosUiState", viewModel.uiState) { viewModel.uiState }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (uiState.value) {
            is Success -> ShowPhotos(navController)
            is Error -> ShowError()
            is Empty -> ShowEmpty()
            else -> ShowLoading()
        }
    }
}

@Composable
fun ShowLoading() {
    Text(
        text = stringResource(id = R.string.loading_message),
        fontSize = 16.sp,
        style = MaterialTheme.typography.bodyLarge
    )
}

@Composable
fun ShowEmpty() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = stringResource(id = R.string.empty_results_message),
            fontSize = 16.sp,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun ShowError(
    viewModel: PhotosViewModel = hiltViewModel()
) {

    val errorMessage = rememberSaveable("error") { viewModel.error }

    Text(
        text = stringResource(id = errorMessage.value?.resId ?: R.string.generic_error),
        fontSize = 14.sp,
        style = MaterialTheme.typography.bodyMedium.copy(
            color = MaterialTheme.colorScheme.error
        )
    )

    Spacer(modifier = Modifier.padding(8.dp))

    Button(
        modifier = Modifier.padding(top = 8.dp),
        enabled = remember { viewModel.fetchInProgress.value },
        onClick = { viewModel.retry() }
    ) {
        Text(text = stringResource(id = R.string.retry_action))
    }
}


