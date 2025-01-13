package com.yousuf.photos.repository

import com.yousuf.photos.common.data.DefaultDispatchers
import com.yousuf.photos.common.events.EventsLogger
import com.yousuf.photos.network.PhotosService
import com.yousuf.photos.network.data.PhotoDetails
import com.yousuf.photos.network.data.toPhotos
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface PhotosRepository {
    suspend fun fetchPhotos(): List<PhotoDetails>

    fun getPhotoDetails(photoId: Int): PhotoDetails?

    fun clear()
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

                try {
                    photosService.getPhotos().let { response ->
                        eventsLogger.logInfo("data received from network")
                        if (response.isSuccessful && response.body() != null) {
                            eventsLogger.logInfo("data received from network")
                            response.body()!!.toPhotos()
                                .apply { cache.putAll(this) }
                                .values.toList()
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

    override fun getPhotoDetails(photoId: Int) = cache[photoId]

    override fun clear() {
        cache.clear()
    }
}

class NetworkException(message: String) : RuntimeException(message)