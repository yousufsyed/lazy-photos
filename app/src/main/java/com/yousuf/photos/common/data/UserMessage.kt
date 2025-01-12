package com.yousuf.photos.common.data

import android.os.Parcelable
import com.yousuf.photos.R
import com.yousuf.photos.model.data.PhotosJsonParseException
import com.yousuf.photos.model.repository.NetworkException
import kotlinx.parcelize.Parcelize
import java.io.IOException

@Parcelize
data class UserMessage(val resId: Int) : Parcelable {
    companion object {
        fun fromThrowable(throwable: Throwable) = when(throwable) {
            is NetworkException,
            is IOException -> UserMessage(R.string.network_error)
            is PhotosJsonParseException -> UserMessage(R.string.parse_error)
            else -> UserMessage(R.string.generic_error)
        }
    }
}