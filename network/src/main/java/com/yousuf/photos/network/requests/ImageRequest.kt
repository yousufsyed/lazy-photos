package com.yousuf.photos.network.requests

import com.yousuf.photos.common.events.EventsLogger
import com.yousuf.photos.network.api.PhotosService
import com.yousuf.photos.network.data.RequestData
import javax.inject.Inject

interface ImageRequest {
    suspend fun fetchBitmapAsBytes(request: RequestData): ByteArray
}

class DefaultImageRequest @Inject constructor(
    private val photosService: PhotosService,
    private val eventsLogger: EventsLogger,
) : ImageRequest {

    override suspend fun fetchBitmapAsBytes(request: RequestData): ByteArray {
        return try {
            photosService.getPhoto(request.imageWidth, request.imageHeight, request.id)
                .let { response ->
                    eventsLogger.logInfo("bitmap received from network")
                    if (response.isSuccessful && response.body() != null) {
                        eventsLogger.logInfo("data received from network")
                        response.body()!!.bytes()
                    } else {
                        throw BitmapFetchException("failed to fetch bitmap")
                    }
                }
        } catch (e: Exception) {
            eventsLogger.logError(e)
            throw e
        }
    }
}

class BitmapFetchException(message: String) : Exception(message)