package com.yolbertdev.coffeeplatform.util

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

actual class ImageStorage(private val context: Context) {
    actual suspend fun saveImage(bytes: ByteArray): String? = withContext(Dispatchers.IO) {
        try {
            val fileName = "img_${UUID.randomUUID()}.jpg"
            val file = File(context.filesDir, fileName)

            FileOutputStream(file).use { stream ->
                stream.write(bytes)
            }
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    actual fun readImage(path: String): ByteArray? {
        return try {
            val file = File(path)
            if (file.exists()) file.readBytes() else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
