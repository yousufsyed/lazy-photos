package com.yousuf.photos.di

import com.yousuf.imageloader.DownloaderFactory
import com.yousuf.imageloader.ImageDownloaderFactory
import com.yousuf.imageloader.di.ImageLoaderModule
import com.yousuf.photos.common.di.CommonModule
import com.yousuf.photos.network.di.NetworkModule
import com.yousuf.photos.repository.DefaultPhotosRepository
import com.yousuf.photos.repository.PhotosRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module(includes = [CommonModule::class, ImageLoaderModule::class, NetworkModule::class])
@InstallIn(SingletonComponent::class)
class PhotosModule {

    @Singleton
    @Provides
    fun providesPhotosRepository(impl: DefaultPhotosRepository): PhotosRepository = impl

    @Singleton
    @Provides
    fun providesImageDownloaderFactory(impl: ImageDownloaderFactory): DownloaderFactory = impl


}
