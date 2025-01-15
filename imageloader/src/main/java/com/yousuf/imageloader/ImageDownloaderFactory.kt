package com.yousuf.imageloader

import com.yousuf.imageloader.cache.BitmapCache
import com.yousuf.imageloader.cache.DiskCache
import com.yousuf.photos.common.di.IO
import com.yousuf.photos.common.events.EventsLogger
import com.yousuf.photos.network.api.PhotosService
import com.yousuf.photos.network.requests.DefaultImageRequest
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

interface DownloaderFactory {
    fun create(): ImageDownloader
}

class ImageDownloaderFactory @Inject constructor(
    private val diskCache: DiskCache,
    private val bitmapCache: BitmapCache,
    private val photosService: PhotosService,
    private val eventLogger: EventsLogger,
    @IO private val ioDispatcher: CoroutineDispatcher,
) : DownloaderFactory {

    override fun create(): ImageDownloader {
        return DefaultImageDownloader(
            DefaultImageRequest(photosService, eventLogger, ioDispatcher),
            bitmapCache,
            eventLogger,
            diskCache,
            ioDispatcher
        )
    }
}