package com.yousuf.photos.viewmodel

import android.content.res.Configuration
import android.graphics.Bitmap
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yousuf.imageloader.DownloaderFactory
import com.yousuf.imageloader.ImageLoaderManager
import com.yousuf.photos.common.data.DefaultDispatchers
import com.yousuf.photos.common.events.EventsLogger
import com.yousuf.photos.common.extensions.getFrameHeight
import com.yousuf.photos.data.PhotoDetails
import com.yousuf.photos.data.toRequestData
import com.yousuf.photos.network.data.RequestData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageLoaderViewModel @Inject constructor(
    private val imageDownloaderFactory: DownloaderFactory,
    private val eventLogger: EventsLogger,
    private val dispatchers: DefaultDispatchers,
) : ViewModel() {

    val imageLoaderManager by lazy {
        ImageLoaderManager.getInstance()
    }

    fun getBitmap(
        configuration: Configuration,
        photoDetails: PhotoDetails,
        state: MutableState<BitmapState>,
    ) {
        viewModelScope.launch(dispatchers.io) {
            state.value = BitmapState.Loading
            val (frameWidth, frameHeight) = getFrameHeight(configuration)
            val requestData = photoDetails.toRequestData(frameWidth, frameHeight)

            imageLoaderManager.getBitmap(
                { fetchBitmap(requestData) },
                { state.onStarted() },
                { bitmap -> state.onSuccess(bitmap) },
                { state.onFailure() }
            )
        }
    }

    fun getThumbnailBitmap(
        configuration: Configuration,
        size: Int,
        photoDetails: PhotoDetails,
        state: MutableState<BitmapState>,
    ) {
        viewModelScope.launch(dispatchers.io) {
            val screenDensity = configuration.densityDpi / 160f
            val size = (size * screenDensity).toInt()
            val requestData = photoDetails.toRequestData(size, size)

            imageLoaderManager.getBitmap(
                { fetchBitmap(requestData) },
                { state.onStarted() },
                { bitmap -> state.onSuccess(bitmap) },
                { state.onFailure() }
            )
        }
    }

    private fun MutableState<BitmapState>.onStarted() {
        this.value = BitmapState.Loading
    }

    private fun MutableState<BitmapState>.onSuccess(bitmap: Bitmap) {
        this.value = BitmapState.Success(bitmap)
    }

    private fun MutableState<BitmapState>.onFailure() {
        eventLogger.logError(RuntimeException("failed to fetch bitmap"))
        this.value = BitmapState.Error("failed to fetch bitmap")
    }

    /***
     * Each request creates a new ImageDownloader instance, to handle image fetch
     * and return a bitmap of desired size to the caller.
     */
    private suspend fun fetchBitmap(request: RequestData): Bitmap {
        return getImageLoader().getBitmap(request)
    }

    private fun getImageLoader() = imageDownloaderFactory.create()

}

sealed class BitmapState {
    object None : BitmapState()
    object Loading : BitmapState()
    data class Success(val bitmap: Bitmap) : BitmapState()
    data class Error(val message: String) : BitmapState()
}