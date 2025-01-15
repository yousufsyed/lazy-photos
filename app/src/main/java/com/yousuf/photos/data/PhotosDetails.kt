package com.yousuf.photos.data

import android.annotation.SuppressLint
import android.os.Parcelable
import com.yousuf.photos.network.data.RawPhoto
import com.yousuf.photos.network.data.RequestData
import kotlinx.parcelize.Parcelize

@Parcelize
data class PhotoDetails(
    val height: Int,
    val width: Int,
    val filename: String,
    val id: Int,
    val author: String,
) : Parcelable

fun List<RawPhoto>.toPhotosMap(): Map<Int, PhotoDetails> = this.map { rawPhoto ->
    rawPhoto.id to PhotoDetails(
        height = rawPhoto.height,
        width = rawPhoto.width,
        filename = rawPhoto.filename,
        id = rawPhoto.id,
        author = rawPhoto.author
    )
}.toMap()

@SuppressLint("DefaultLocale")
fun PhotoDetails.toRequestData(
    frameHeight: Int = 0,
    frameWidth: Int = 0,
) = RequestData(
    id = id,
    imageWidth = width,
    imageHeight = height,
    frameHeight = frameHeight,
    frameWidth = frameWidth,
    filename = "PICSUM_PHOTOS_$filename"
)