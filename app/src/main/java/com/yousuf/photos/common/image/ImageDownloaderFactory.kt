package com.yousuf.photos.common.image

import com.yousuf.photos.common.data.DefaultDispatchers
import com.yousuf.photos.common.events.EventsLogger
import com.yousuf.photos.model.network.DefaultImageRequest
import com.yousuf.photos.model.network.PhotosService
import javax.inject.Inject

interface DownloaderFactory {
    fun create() : ImageDownloader
}

class ImageDownloaderFactory @Inject constructor(
    private val diskCache: DiskCache,
    private val bitmapCache: BitmapCache,
    private val eventLogger: EventsLogger,
    private val photosService: PhotosService,
    private val dispatchers: DefaultDispatchers,
) : DownloaderFactory  {

    override fun create(): ImageDownloader {
        return DefaultImageDownloader(
            DefaultImageRequest(photosService, eventLogger),
            bitmapCache,
            eventLogger,
            diskCache,
            dispatchers
        )
    }
}