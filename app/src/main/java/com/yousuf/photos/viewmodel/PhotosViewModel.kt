package com.yousuf.photos.viewmodel

import android.os.Parcelable
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yousuf.photos.common.data.UserMessage
import com.yousuf.photos.common.events.EventsLogger
import com.yousuf.photos.model.data.PhotoDetails
import com.yousuf.photos.model.repository.PhotosRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

@HiltViewModel
class PhotosViewModel @Inject constructor(
    private val photosRepository: PhotosRepository,
    private val eventLogger: EventsLogger
) : ViewModel() {

    var photosFlow = mutableStateOf<List<PhotoDetails>>(emptyList())
        private set

    var uiState = mutableStateOf<UiState>(UiState.Loading)
        private set

    var error = mutableStateOf<UserMessage?>(null)
        private set

    var fetchInProgress = mutableStateOf<Boolean>(false)
        private set

    var photoDetails = mutableStateOf<PhotoDetails?>(null)
        private set

    init {
        fetchPhotos()
    }

    fun retry() {
        fetchPhotos()
    }

    fun fetchPhotos() {
        viewModelScope.launch {
            fetchInProgress.value = true
            updateUiState(UiState.Loading)
            error.value = null
            try {
                photosFlow.value = photosRepository.fetchPhotos().also {
                    updateUiState( if(it.isEmpty()) UiState.Empty else UiState.Success )
                }
            } catch (e: Exception) {
                error.value = UserMessage.fromThrowable(e)
                updateUiState(UiState.Error)
            }
        }
    }

    private fun updateUiState(state: UiState) {
        uiState.value = state
        fetchInProgress.value = false
    }

    fun getPhotoDetails(photoId: Int) {
        eventLogger.logInfo("fetching PhotoDetails: $photoId")
        photosRepository.getPhotoDetails(photoId).apply {
            photoDetails.value = this
            eventLogger.logInfo("received PhotoDetails: $photoId")
        }
    }

    @Parcelize sealed class UiState : Parcelable {
        object Loading : UiState()
        object Success : UiState()
        object Empty : UiState()
        object Error : UiState()
    }

}