package com.yolbertdev.coffeeplatform.util

// En commonMain
import coil3.Bitmap

expect class ImageStorage {
    suspend fun saveImage(bytes: ByteArray): String?
    fun readImage(path: String): ByteArray?
}