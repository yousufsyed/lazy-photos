package com.yousuf.imageloader.di

import com.yousuf.imageloader.cache.BitmapCache
import com.yousuf.imageloader.cache.DefaultBitmapCache
import com.yousuf.imageloader.cache.DefaultDiskCache
import com.yousuf.imageloader.cache.DiskCache
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ImageLoaderModule {

    @Singleton
    @Provides
    fun providesBitmapCache(impl: DefaultBitmapCache): BitmapCache = impl

    @Singleton
    @Provides
    fun providesDiskCache(impl: DefaultDiskCache): DiskCache = impl
}


