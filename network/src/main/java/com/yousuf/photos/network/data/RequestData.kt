package com.yousuf.photos.network.data

data class RequestData(
    val id: Int,
    val frameHeight: Int, // height of the imageview container
    val frameWidth: Int, // width of the imageview container
    val imageHeight: Int,
    val imageWidth: Int,
    val filename: String,
)

