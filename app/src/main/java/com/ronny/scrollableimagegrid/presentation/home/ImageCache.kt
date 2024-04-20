package com.ronny.scrollableimagegrid.presentation.home

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class ImageCache(private val context: Context) {
    private val memoryCache = mutableMapOf<String, Bitmap>()
    private val diskCacheDirectory = File(context.cacheDir, "image_cache")

    init {
        diskCacheDirectory.mkdirs()
    }

    suspend fun getImage(url: String): Bitmap? = withContext(Dispatchers.IO) {
        var bitmap: Bitmap? = memoryCache[url]
        if (bitmap == null) {
            bitmap = loadFromDiskCache(url)
            if (bitmap != null) {
                memoryCache[url] = bitmap
            }
        }
        bitmap
    }

    suspend fun setImage(url: String, bitmap: Bitmap) = withContext(Dispatchers.IO) {
        memoryCache[url] = bitmap
        saveToDiskCache(url, bitmap)
    }

    private fun loadFromDiskCache(url: String): Bitmap? {
        val file = File(diskCacheDirectory, getFileName(url))
        return if (file.exists()) {
            FileInputStream(file).use { stream ->
                BitmapFactory.decodeStream(stream)
            }
        } else {
            null
        }
    }

    private fun saveToDiskCache(url: String, bitmap: Bitmap) {
        val file = File(diskCacheDirectory, getFileName(url))
        FileOutputStream(file).use { stream ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.flush()
        }
    }

    private fun getFileName(url: String): String {
        return url.hashCode().toString()
    }
}