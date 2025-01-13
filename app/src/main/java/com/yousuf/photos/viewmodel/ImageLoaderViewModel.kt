package com.yousuf.photos.viewmodel

import android.content.res.Configuration
import android.graphics.Bitmap
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yousuf.photos.common.data.DefaultDispatchers
import com.yousuf.photos.common.events.EventsLogger
import com.yousuf.photos.common.extensions.getFrameHeight
import com.yousuf.photos.imageLoader.DownloaderFactory
import com.yousuf.photos.network.data.PhotoDetails
import com.yousuf.photos.network.data.RequestData
import com.yousuf.photos.network.data.toRequestData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageLoaderViewModel @Inject constructor(
    private val imageDownloaderFactory: DownloaderFactory,
    private val eventLogger: EventsLogger,
    private val dispatchers: DefaultDispatchers,
) : ViewModel() {

    fun getBitmap(
        configuration: Configuration,
        photoDetails: PhotoDetails,
        state: MutableState<BitmapState>,
    ) {
        viewModelScope.launch(dispatchers.io) {
            state.value = BitmapState.Loading
            try {
                val (frameWidth, frameHeight) = getFrameHeight(configuration)
                val request =
                    photoDetails.toRequestData(frameWidth = frameWidth, frameHeight = frameHeight)
                fetchBitmap(request).let { bitmap ->
                    state.value = BitmapState.Success(bitmap)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                eventLogger.logError(RuntimeException("failed to fetch bitmap"))
                state.value = BitmapState.Error("failed to fetch bitmap")
            }
        }
    }

    fun getThumbnailBitmap(
        configuration: Configuration,
        size: Int,
        photoDetails: PhotoDetails,
        state: MutableState<BitmapState>,
    ) {
        viewModelScope.launch(dispatchers.io) {
            state.value = BitmapState.Loading
            try {
                val screenDensity = configuration.densityDpi / 160f
                val size = (size * screenDensity).toInt()
                val request = photoDetails.toRequestData(size, size)
                fetchBitmap(request).let { bitmap ->
                    state.value = BitmapState.Success(bitmap)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                eventLogger.logError(RuntimeException("failed to fetch bitmap"))
                state.value = BitmapState.Error("failed to fetch bitmap")
            }
        }
    }

    /***
     * Each request creates a new ImageDownloader instance, to handle image fetch
     * and return a bitmap of desired size to the caller.
     */
    private suspend fun fetchBitmap(request: RequestData): Bitmap {
        return imageDownloaderFactory.create().getBitmap(request)
    }

}

sealed class BitmapState {
    object None : BitmapState()
    object Loading : BitmapState()
    data class Success(val bitmap: Bitmap) : BitmapState()
    data class Error(val message: String) : BitmapState()
}