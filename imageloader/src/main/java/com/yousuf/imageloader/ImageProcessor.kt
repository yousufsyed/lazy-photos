package com.yousuf.imageloader

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.yousuf.photos.network.data.RequestData

interface ImageProcessor {
    suspend fun processBitmapFromBytes(
        bytes: ByteArray,
        request: RequestData,
    ): Bitmap
}

/**
 * DefaultImageProcessor is a helper class to process bytes into bitmap,
 * scale it to desired size, and return a bitmap.
 */
class DefaultImageProcessor() : ImageProcessor {

    override suspend fun processBitmapFromBytes(
        bytes: ByteArray,
        request: RequestData,
    ): Bitmap {
        try {
            val options = getBitmapOptions(request)
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.size, options)
        } catch (e: Exception) {
            throw BitmapDecodeException()
        }
    }

    private fun getBitmapOptions(request: RequestData): BitmapFactory.Options {
        return with(request) {
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
                inSampleSize =
                    calculateInSampleSize(frameHeight, frameWidth, imageWidth, imageHeight)
            }
            options.inJustDecodeBounds = false
            options
        }
    }

    private fun calculateInSampleSize(
        frameHeight: Int,
        frameWidth: Int,
        imageWidth: Int,
        imageHeight: Int,
    ): Int {
        var inSampleSize = 1
        var (width, height) = imageWidth to imageHeight

        while (height / 2 > frameHeight && width / 2 > frameWidth) {
            height /= 2
            width /= 2
            inSampleSize *= 2
        }
        return inSampleSize
    }
}

class BitmapDecodeException() : RuntimeException()