package com.yousuf.photos.imageLoader

import android.graphics.Bitmap
import java.util.Collections
import javax.inject.Inject

interface BitmapCache {
    fun contains(key: String): Boolean
    fun get(key: String): Bitmap?
    fun put(key: String, bitmap: Bitmap)
    fun clear()
}

class DefaultBitmapCache @Inject constructor() : BitmapCache {

    private val limit by lazy { Runtime.getRuntime().maxMemory() / 4 } // 25% of the total heap.

    private var size = 0L

    private val synchronizedMap by lazy {
        Collections.synchronizedMap(object : LinkedHashMap<String, Bitmap>(10, 1.5f, true) {
            override fun removeEldestEntry(eldest: Map.Entry<String, Bitmap>?): Boolean {
                return size > limit
            }
        })
    }

    override fun contains(key: String) = synchronizedMap.containsKey(key)

    override fun get(key: String) = synchronizedMap[key]

    override fun put(key: String, bitmap: Bitmap) {
        if (synchronizedMap[key] == null) {
            synchronizedMap[key] = bitmap
            size += getBytes(bitmap)
        }
    }

    private fun getBytes(bmp: Bitmap): Long {
        return (bmp.rowBytes * bmp.height).toLong()
    }

    override fun clear() {
        synchronizedMap.clear()
    }
}