package com.yolbertdev.coffeeplatform.ui.main.screens.customer.add

import androidx.compose.runtime.Composable
import coil3.Bitmap
import org.jetbrains.skia.Image
import org.jetbrains.skia.Bitmap as SkiaBitmap
import java.awt.FileDialog
import java.awt.Frame
import java.io.File

@Composable
actual fun rememberCameraLauncher(onResult: (Bitmap?) -> Unit): () -> Unit {
    return {
        val dialog = FileDialog(null as Frame?, "Seleccionar Foto", FileDialog.LOAD)
        dialog.isVisible = true
        if (dialog.file != null) {
            val file = File(dialog.directory, dialog.file)
            val bytes = file.readBytes()

            // CONVERSIÓN CORRECTA PARA SKIA
            val skiaImage = Image.makeFromEncoded(bytes)
            val bitmap = SkiaBitmap.makeFromImage(skiaImage) // Convertimos Image a Bitmap
            onResult(bitmap)
        }
    }
}