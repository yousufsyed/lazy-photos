package com.yousuf.photos.common.image

import android.graphics.Bitmap
import com.yousuf.photos.common.data.DefaultDispatchers
import com.yousuf.photos.common.events.EventsLogger
import com.yousuf.photos.model.data.RequestData
import com.yousuf.photos.model.network.BitmapFetchException
import com.yousuf.photos.model.network.ImageRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface ImageDownloader {
    suspend fun getBitmap(request: RequestData): Bitmap
}

/**
 * ImageDownloader is responsible for fetching the image from the network,
 * processing the image,
 * Saving the image to disk,
 * Caching the image in memory,
 * and returning the bitmap to caller
 */
class DefaultImageDownloader @Inject constructor(
    private val imageRequest: ImageRequest,
    private val bitmapCache: BitmapCache,
    private val eventLogger: EventsLogger,
    private val diskCache: DiskCache,
    private val dispatchers: DefaultDispatchers,
) : ImageDownloader {

    override suspend fun getBitmap(request: RequestData): Bitmap {
        return withContext(dispatchers.io) {
            val cacheKey = request.filename + request.frameWidth
            bitmapCache.get(cacheKey) ?: // try cache
            diskFetch(request.filename)?.toProcessedBitmap(cacheKey, request) ?: //try disk
            networkFetch(request).toProcessedBitmap(cacheKey, request) //try network
        }
    }

    private suspend fun diskFetch(filename: String): ByteArray? {
        try {
            eventLogger.logInfo("reading from disk")
            return diskCache.readFromDisk(filename)
        } catch (e: Exception) {
            eventLogger.logError(e)
        }
        return null
    }

    private suspend fun networkFetch(request: RequestData): ByteArray {
        try {
            eventLogger.logInfo("reading from network")
            return imageRequest.fetchBitmapAsBytes(request)
                .also { bytes -> saveBitmap(bytes, request.filename) }
        } catch (e: Exception) {
            eventLogger.logError(e)
            throw BitmapFetchException("failed to fetch bitmap")
        }
    }

    private fun saveBitmap(bytes: ByteArray, filename: String) {
        eventLogger.logInfo("saving bitmap to disk")
        try {
            // since this can be independent of displaying the bitmap
            // this can be delegated to a new coroutine
            // Fixme handle cancellation
            CoroutineScope(SupervisorJob() + dispatchers.io).launch {
                diskCache.saveToDisk(bytes, filename)
            }
        } catch (e: Exception) {
            eventLogger.logError(e)
        }
    }

    private suspend fun ByteArray.toProcessedBitmap(
        cacheKey: String,
        request: RequestData,
    ): Bitmap {
        eventLogger.logInfo("converting to bitmap")
        val imageProcessor = DefaultImageProcessor()
        return imageProcessor.processBitmapFromBytes(this, request)
            .also {
                // cache separate image for thumbnail and details
                bitmapCache.put(cacheKey, it)
            }
    }
}