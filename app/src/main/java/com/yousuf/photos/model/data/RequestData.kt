package com.yousuf.photos.model.data

import android.annotation.SuppressLint
import com.yousuf.photos.common.extensions.getOffset

private const val FILENAME = "PICSUM_PHOTOS_%d_%s"

data class RequestData(
    val id: Int,
    val frameHeight: Int, // height of the imageview container
    val frameWidth: Int, // width of the imageview container
    val imageHeight: Int,
    val imageWidth: Int,
    val filename: String
)

@SuppressLint("DefaultLocale")
fun PhotoDetails.toRequestData(
    frameHeight: Int = 0,
    frameWidth: Int = 0
) = RequestData(
    id = id,
    imageWidth = width,
    imageHeight = height,
    frameHeight = frameHeight,
    frameWidth = frameWidth,
    filename = String.format(FILENAME, frameWidth,filename),
)

