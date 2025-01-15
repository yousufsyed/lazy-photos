package com.yousuf.photos.network.data

import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import okhttp3.ResponseBody

data class RawPhoto(
    val format: String,
    val height: Int,
    val width: Int,
    val filename: String,
    val id: Int,
    val post_url: String,
    val auther_url: String,
    val author: String,
)

fun ResponseBody.toPhotos(): List<RawPhoto> {
    try {
        val jsonBody = string()
        val typeToken = object : TypeToken<List<RawPhoto>>() {}.type
        val photos = Gson().fromJson<List<RawPhoto>>(jsonBody, typeToken)
        return photos
    } catch (e: Exception) {
        throw PhotosJsonParseException()
    }
}

class PhotosJsonParseException() : RuntimeException()