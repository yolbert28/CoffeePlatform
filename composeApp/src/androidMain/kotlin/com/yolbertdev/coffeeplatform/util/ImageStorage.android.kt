package com.yolbertdev.coffeeplatform.util

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

actual object ImageStorage {
    lateinit var appContext: Context

    actual suspend fun saveImage(bitmap: coil3.Bitmap, fileName: String): String? {
        val androidBitmap = bitmap
        val file = File(appContext.filesDir, "$fileName.webp")

        print("imageeeeeeee")

        return try {
            print("start")
            FileOutputStream(file).use { out ->
                val format = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

                    print("here")
                    Bitmap.CompressFormat.WEBP_LOSSY
                } else {
                    @Suppress("DEPRECATION")
                    print("here 2")
                    Bitmap.CompressFormat.WEBP
                }


                print("ending")
                androidBitmap.compress(format, 80, out)
            }
            val img = file.absolutePath
            print("imageeeeeeee: $img")
            img
        } catch (e: Exception) {
            e.printStackTrace()
            "nadaaaa"
        }
    }
}
