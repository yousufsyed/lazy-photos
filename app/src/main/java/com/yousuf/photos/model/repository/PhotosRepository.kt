package com.yousuf.photos.model.repository

import com.yousuf.photos.common.data.DefaultDispatchers
import com.yousuf.photos.common.events.EventsLogger
import com.yousuf.photos.model.data.PhotoDetails
import com.yousuf.photos.model.data.toPhotos
import com.yousuf.photos.model.network.PhotosService
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface PhotosRepository {
    suspend fun fetchPhotos(): List<PhotoDetails>

    fun getPhotoDetails(photoId: Int): PhotoDetails?
}

class DefaultPhotosRepository @Inject constructor(
    private val photosService: PhotosService,
    private val eventsLogger: EventsLogger,
    private val dispatchers: DefaultDispatchers,
) : PhotosRepository {

    private val cache = mutableMapOf<Int, PhotoDetails>()

    override suspend fun fetchPhotos(): List<PhotoDetails> {
        return withContext(dispatchers.io) {
            if (cache.isNotEmpty()) {
                eventsLogger.logInfo("returning cached data")
                cache.values.toList()
            } else {
                eventsLogger.logInfo("fetching data from network")
                photosService.getPhotos().let { response ->
                    try {
                        if (response.isSuccessful && response.body() != null) {
                            eventsLogger.logInfo("data received from network")
                            response.body()!!.toPhotos()
                                .apply { cache.putAll(this) }
                                .let { it.values.toList() }
                        } else {
                            throw NetworkException("failed to receive data from network")
                        }
                    } catch (e: Exception) {
                        eventsLogger.logError(e)
                        throw e
                    }
                }
            }
        }
    }

    override fun getPhotoDetails(photoId: Int) = cache[photoId]
}

class NetworkException(message: String) : Exception(message)