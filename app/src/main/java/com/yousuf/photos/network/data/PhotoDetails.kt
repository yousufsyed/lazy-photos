package com.yousuf.photos.network.data

import android.os.Parcelable
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize
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

@Parcelize
data class PhotoDetails(
    val height: Int,
    val width: Int,
    val filename: String,
    val id: Int,
    val author: String,
) : Parcelable {
    val downloadUrl: String
        get() = "https://picsum.photos/id/$id/$width/$height"
}

fun ResponseBody.toPhotos(): Map<Int, PhotoDetails> {
    try {
        val jsonBody = string()
        val typeToken = object : TypeToken<List<RawPhoto>>() {}.type
        val photos = Gson().fromJson<List<RawPhoto>>(jsonBody, typeToken)

        return photos.map { rawPhoto ->
            rawPhoto.id to PhotoDetails(
                height = rawPhoto.height,
                width = rawPhoto.width,
                filename = rawPhoto.filename,
                id = rawPhoto.id,
                author = rawPhoto.author
            )
        }.toMap()

    } catch (e: Exception) {
        throw PhotosJsonParseException()
    }
}


class PhotosJsonParseException() : RuntimeException()