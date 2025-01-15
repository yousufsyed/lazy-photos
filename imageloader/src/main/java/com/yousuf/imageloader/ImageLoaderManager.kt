package com.yousuf.imageloader

import android.graphics.Bitmap

class ImageLoaderManager {

    suspend fun getBitmap(
        request: suspend () -> Bitmap,
        onStarted: () -> Unit,
        onSuccess: (Bitmap) -> Unit,
        onFailure: () -> Unit,
    ) {
        try {
            onStarted()
            request().let { bitmap -> onSuccess(bitmap) }
        } catch (e: Exception) {
            onFailure()
        }
    }

    companion object {
        private val LOCK = Any()

        @Volatile
        private var INSTANCE: ImageLoaderManager? = null

        fun getInstance(): ImageLoaderManager {
            return synchronized(LOCK) {
                INSTANCE ?: ImageLoaderManager()
            }
        }
    }
}