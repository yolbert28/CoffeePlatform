package com.yolbertdev.coffeeplatform.util

// En commonMain
import coil3.Bitmap

expect object ImageStorage {
    suspend fun saveImage(bitmap: Bitmap, fileName: String): String?
}