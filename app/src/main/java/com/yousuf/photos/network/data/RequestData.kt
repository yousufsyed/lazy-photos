package com.yousuf.photos.network.data

import android.annotation.SuppressLint

private const val FILENAME = "PICSUM_PHOTOS_%s"

data class RequestData(
    val id: Int,
    val frameHeight: Int, // height of the imageview container
    val frameWidth: Int, // width of the imageview container
    val imageHeight: Int,
    val imageWidth: Int,
    val filename: String,
)

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
    filename = String.format(FILENAME, filename),
)

