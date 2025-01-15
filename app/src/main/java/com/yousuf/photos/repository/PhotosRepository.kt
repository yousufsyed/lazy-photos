package com.yousuf.photos.repository

import com.yousuf.photos.common.di.IO
import com.yousuf.photos.common.events.EventsLogger
import com.yousuf.photos.data.PhotoDetails
import com.yousuf.photos.data.toPhotosMap
import com.yousuf.photos.network.requests.PhotosRequest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface PhotosRepository {
    suspend fun fetchPhotos(): List<PhotoDetails>

    fun getPhotoDetails(photoId: Int): PhotoDetails?

    fun clear()
}

class DefaultPhotosRepository @Inject constructor(
    private val photosRequest: PhotosRequest,
    private val eventsLogger: EventsLogger,
    @IO private val ioDispatchers: CoroutineDispatcher,
) : PhotosRepository {

    private val cache = mutableMapOf<Int, PhotoDetails>()

    override suspend fun fetchPhotos(): List<PhotoDetails> {
        return withContext(ioDispatchers) {
            if (cache.isNotEmpty()) {
                eventsLogger.logInfo("returning cached data")
                cache.values.toList()
            } else {
                eventsLogger.logInfo("fetching data from network")
                try {
                    photosRequest.fetchPhotos()
                        .toPhotosMap()
                        .apply { cache.putAll(this) }
                        .also { eventsLogger.logInfo("data received from network") }
                        .values.toList()
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
