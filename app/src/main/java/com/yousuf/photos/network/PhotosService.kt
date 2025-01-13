package com.yousuf.photos.network

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PhotosService {

    @GET("/list")
    suspend fun getPhotos(): Response<ResponseBody>

    @GET("/{width}/{height}")
    suspend fun getPhoto(
        @Path("width") width: Int,
        @Path("height") height: Int,
        @Query("image") id: Int,
    ): Response<ResponseBody>
}