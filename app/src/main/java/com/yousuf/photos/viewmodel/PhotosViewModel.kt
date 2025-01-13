package com.yousuf.photos.viewmodel

import android.os.Parcelable
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yousuf.photos.common.data.UserMessage
import com.yousuf.photos.common.events.EventsLogger
import com.yousuf.photos.model.data.ImageLoader
import com.yousuf.photos.network.data.PhotoDetails
import com.yousuf.photos.repository.PhotosRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

@HiltViewModel
class PhotosViewModel @Inject constructor(
    private val photosRepository: PhotosRepository,
    private val eventLogger: EventsLogger,
    imageLoader: ImageLoader,
) : ViewModel() {

    val imageLoaderType = imageLoader

    var photosFlow = mutableStateOf<List<PhotoDetails>>(emptyList())
        private set

    var uiState = mutableStateOf<UiState>(UiState.None)
        private set

    var error = mutableStateOf<UserMessage?>(null)
        private set

    var canRetry = mutableStateOf<Boolean>(true)
        private set

    var photoDetails = mutableStateOf<PhotoDetails?>(null)
        private set

    init {
        fetchPhotos()
    }

    fun retry() {
        fetchPhotos()
    }

    fun refreshList() {
        photosRepository.clear()
        fetchPhotos()
    }

    fun fetchPhotos() {
        viewModelScope.launch {
            canRetry.value = false
            updateUiState(UiState.Loading)
            error.value = null
            try {
                photosFlow.value = photosRepository.fetchPhotos().also {
                    updateUiState(
                        if (it.isEmpty()) UiState.Empty else UiState.Success
                    )
                }
            } catch (e: Exception) {
                error.value = UserMessage.fromThrowable(e)
                updateUiState(UiState.Error)
            }
        }
    }

    private fun updateUiState(state: UiState) {
        uiState.value = state
        canRetry.value = true
    }

    fun fetchPhotoDetails(photoId: Int) {
        eventLogger.logInfo("fetching PhotoDetails: $photoId")
        photosRepository.getPhotoDetails(photoId).apply {
            photoDetails.value = this
            eventLogger.logInfo("received PhotoDetails: $photoId")
        }
    }

    fun clearPhotoDetails() {
        photoDetails.value = null
    }

    @Parcelize
    sealed class UiState : Parcelable {
        object Loading : UiState()
        object Success : UiState()
        object Empty : UiState()
        object Error : UiState()
        object None : UiState()
    }
}