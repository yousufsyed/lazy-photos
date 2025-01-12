package com.yousuf.photos.di

import com.yousuf.photos.common.data.DefaultDispatchers
import com.yousuf.photos.common.events.DefaultEventsLogger
import com.yousuf.photos.common.events.DefaultMessageDelegate
import com.yousuf.photos.common.events.EventsLogger
import com.yousuf.photos.common.events.LogEvent
import com.yousuf.photos.common.events.MessageDelegate
import com.yousuf.photos.common.events.SnackbarEvent
import com.yousuf.photos.common.image.BitmapCache
import com.yousuf.photos.common.image.DefaultBitmapCache
import com.yousuf.photos.common.image.DefaultDiskCache
import com.yousuf.photos.common.image.DiskCache
import com.yousuf.photos.common.image.DownloaderFactory
import com.yousuf.photos.common.image.ImageDownloaderFactory
import com.yousuf.photos.model.network.DefaultImageRequest
import com.yousuf.photos.model.network.ImageRequest
import com.yousuf.photos.model.network.PhotosService
import com.yousuf.photos.model.repository.DefaultPhotosRepository
import com.yousuf.photos.model.repository.PhotosRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import retrofit2.Retrofit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PhotosModule {

    @Provides
    fun providesRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://picsum.photos")
            .build()
    }

    @Singleton
    @Provides
    fun providesPhotosService(retrofit: Retrofit): PhotosService {
        return retrofit.create(PhotosService::class.java)
    }

    @Provides
    @Singleton
    @EventLoggerScope
    fun providesEventLoggerChannel() = Channel<LogEvent>()

    @Provides
    @Singleton
    @MessageDelegateScope
    fun providesMessageDelegateChannel() = Channel<SnackbarEvent>()

    @Provides
    @Singleton
    fun providesEventLogger(impl: DefaultEventsLogger): EventsLogger = impl

    @Provides
    @Singleton
    fun providesSnackbarDelegate(impl: DefaultMessageDelegate): MessageDelegate = impl

    @Singleton
    @Provides
    fun providesDispatchers(): DefaultDispatchers = DefaultDispatchers(
        default = Dispatchers.Default,
        io = Dispatchers.IO,
        main = Dispatchers.Main
    )

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

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class EventLoggerScope

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class MessageDelegateScope