package com.yousuf.photos.network.di

import com.yousuf.photos.network.api.PhotosService
import com.yousuf.photos.network.requests.DefaultImageRequest
import com.yousuf.photos.network.requests.DefaultPhotosRequest
import com.yousuf.photos.network.requests.ImageRequest
import com.yousuf.photos.network.requests.PhotosRequest
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

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
    fun providesPhotosRequest(impl: DefaultPhotosRequest): PhotosRequest = impl

    @Provides
    fun providesImageRequest(impl: DefaultImageRequest): ImageRequest = impl
}