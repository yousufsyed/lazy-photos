package com.yousuf.photos.network.requests

import com.yousuf.photos.common.di.IO
import com.yousuf.photos.common.events.EventsLogger
import com.yousuf.photos.network.api.PhotosService
import com.yousuf.photos.network.data.RawPhoto
import com.yousuf.photos.network.data.toPhotos
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface PhotosRequest {
    suspend fun fetchPhotos(): List<RawPhoto>
}

class DefaultPhotosRequest @Inject constructor(
    private val photosService: PhotosService,
    private val eventsLogger: EventsLogger,
    @IO private val ioDispatcher: CoroutineDispatcher,
) : PhotosRequest {

    override suspend fun fetchPhotos(): List<RawPhoto> {
        return withContext(ioDispatcher) {
            eventsLogger.logInfo("fetching data from network")

            try {
                photosService.getPhotos().let { response ->
                    eventsLogger.logInfo("data received from network")
                    if (response.isSuccessful && response.body() != null) {
                        eventsLogger.logInfo("data received from network")
                        response.body()!!.toPhotos()
                    } else {
                        throw NetworkException("Failed to fetch photos")
                    }
                }
            } catch (e: Exception) {
                eventsLogger.logError(e)
                throw e
            }
        }
    }
}

class NetworkException(message: String) : RuntimeException(message)