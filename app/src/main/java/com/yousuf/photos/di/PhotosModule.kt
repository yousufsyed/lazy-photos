package com.yousuf.photos.di

import com.yousuf.photos.common.di.CommonModule
import com.yousuf.photos.imageLoader.BitmapCache
import com.yousuf.photos.imageLoader.DefaultBitmapCache
import com.yousuf.photos.imageLoader.DefaultDiskCache
import com.yousuf.photos.imageLoader.DiskCache
import com.yousuf.photos.imageLoader.DownloaderFactory
import com.yousuf.photos.imageLoader.ImageDownloaderFactory
import com.yousuf.photos.network.requests.DefaultImageRequest
import com.yousuf.photos.network.requests.ImageRequest
import com.yousuf.photos.network.di.NetworkModule
import com.yousuf.photos.repository.DefaultPhotosRepository
import com.yousuf.photos.repository.PhotosRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module(includes = [CommonModule::class, NetworkModule::class])
@InstallIn(SingletonComponent::class)
class PhotosModule {

    @Singleton
    @Provides
    fun providesPhotosRepository(impl: DefaultPhotosRepository): PhotosRepository = impl

    @Singleton
    @Provides
    fun providesBitmapCache(impl: DefaultBitmapCache): BitmapCache = impl

    @Singleton
    @Provides
    fun providesDiskCache(impl: DefaultDiskCache): DiskCache = impl

    @Singleton
    @Provides
    fun providesImageDownloaderFactory(impl: ImageDownloaderFactory): DownloaderFactory = impl

    @Provides
    fun providesImageRequest(impl: DefaultImageRequest): ImageRequest = impl
}
