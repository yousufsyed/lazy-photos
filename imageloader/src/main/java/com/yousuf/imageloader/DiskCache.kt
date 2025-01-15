package com.yousuf.imageloader

import android.content.Context
import android.os.Environment
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

interface DiskCache {
    suspend fun saveToDisk(bytes: ByteArray, fileName: String)
    suspend fun readFromDisk(fileName: String): ByteArray?
}

class DefaultDiskCache @Inject constructor(
    @ApplicationContext context: Context,
) : DiskCache {

    private val cacheDir: File

    init {
        cacheDir = getStorageDir(context) ?: createStorageDir(context)
        if (!cacheDir.exists()) {
            cacheDir.mkdirs()
        }
    }

    fun getFile(fileName: String): File? {
        return File(cacheDir, fileName)
    }

    override suspend fun saveToDisk(bytes: ByteArray, fileName: String) {
        getFile(fileName)?.writeBytes(bytes)
    }

    override suspend fun readFromDisk(fileName: String): ByteArray? {
        return getFile(fileName)?.readBytes()
    }

    // Get the directory for the app's private pictures directory.
    private fun getStorageDir(context: Context): File? {
        return try {
            File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "/PhotoDownloads")
        } catch (e: Exception) {
            null
        }
    }

    private fun createStorageDir(context: Context): File {
        return File(context.filesDir, "/data/PhotoDownloads")
    }

    // TODO Clear all the files in the cache directory every 24 hours
    fun clear() {
        cacheDir.listFiles()?.forEach {
            it.delete()
        }
    }
}