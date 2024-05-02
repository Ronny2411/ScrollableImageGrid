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
    private val imageMemoryCache = mutableMapOf<String, Bitmap>()
    private val cacheDirectory = File(context.cacheDir, "image_cache")

    init {
        cacheDirectory.mkdirs()
    }

    suspend fun getImage(url: String): Bitmap? = withContext(Dispatchers.IO) {
        var bitmap: Bitmap? = imageMemoryCache[url]
        if (bitmap == null) {
            bitmap = loadImageFromDiskCache(url)
            if (bitmap != null) {
                imageMemoryCache[url] = bitmap
            }
        }
        bitmap
    }

    suspend fun setImage(url: String, bitmap: Bitmap) = withContext(Dispatchers.IO) {
        imageMemoryCache[url] = bitmap
        saveImageToDiskCache(url, bitmap)
    }

    private fun loadImageFromDiskCache(url: String): Bitmap? {
        val file = File(cacheDirectory, getFileName(url))
        return if (file.exists()) {
            FileInputStream(file).use { stream ->
                BitmapFactory.decodeStream(stream)
            }
        } else {
            null
        }
    }

    private fun saveImageToDiskCache(url: String, bitmap: Bitmap) {
        val file = File(cacheDirectory, getFileName(url))
        FileOutputStream(file).use { stream ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.flush()
        }
    }

    private fun getFileName(url: String): String {
        return url.hashCode().toString()
    }
}