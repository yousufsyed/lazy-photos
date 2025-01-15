package com.yousuf.photos.common.extensions

import android.content.res.Configuration

fun getOffset(imageWidth: Int, imageHeight: Int, frameHeight: Int): Int {
    val ratio = imageWidth.toFloat() / imageHeight.toFloat()
    return if (ratio > 1) {
        frameHeight - (frameHeight.toFloat() / ratio).toInt()
    } else {
        0
    }
}

fun getFrameHeight(configuration: Configuration): Pair<Int, Int> {
    val screenDensity = configuration.densityDpi / 160f
    val frameHeight = configuration.screenHeightDp.toFloat() * screenDensity
    val frameWidth = configuration.screenWidthDp.toFloat() * screenDensity
    return (frameHeight * 0.6f).toInt() to (frameWidth * 1f).toInt()
}