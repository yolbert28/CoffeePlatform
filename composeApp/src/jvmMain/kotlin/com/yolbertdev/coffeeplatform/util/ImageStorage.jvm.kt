package com.yolbertdev.coffeeplatform.util

import org.jetbrains.skia.EncodedImageFormat
import org.jetbrains.skia.Image
import java.io.File

actual object ImageStorage {
    actual suspend fun saveImage(bitmap: coil3.Bitmap, fileName: String): String? {
        val skiaBitmap = bitmap // Coil 3 Bitmap es org.jetbrains.skia.Bitmap en Desktop
        val image = Image.makeFromBitmap(skiaBitmap)
        val data = image.encodeToData(EncodedImageFormat.JPEG, 80) ?: return null

        val file = File(System.getProperty("user.home"), ".coffeeplatform/images/$fileName.jpg")
        file.parentFile.mkdirs()

        return try {
            file.writeBytes(data.bytes)
            file.absolutePath
        } catch (e: Exception) {
            null
        }
    }
}