package com.yolbertdev.coffeeplatform.util

import java.io.File
import java.util.UUID

actual class ImageStorage {
    actual suspend fun saveImage(bytes: ByteArray): String? {
        val fileName = "img_${UUID.randomUUID()}.jpg"
        val file = File(System.getProperty("user.home"), ".coffeeplatform/images/$fileName")
        file.parentFile.mkdirs()

        return try {
            file.writeBytes(bytes)
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    actual fun readImage(path: String): ByteArray? {
        return try {
            File(path).readBytes()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
