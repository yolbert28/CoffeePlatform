package com.yolbertdev.coffeeplatform.ui.main.screens.customer.add

import androidx.compose.runtime.Composable
import java.io.File
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

@Composable
actual fun rememberGalleryLauncher(onResult: (ByteArray?) -> Unit): () -> Unit {
    return {
        // Implementación simple usando Swing para Desktop
        val fileChooser = JFileChooser()
        fileChooser.dialogTitle = "Seleccionar Imagen"
        fileChooser.fileFilter = FileNameExtensionFilter("Imágenes", "jpg", "png", "jpeg")

        val result = fileChooser.showOpenDialog(null)

        if (result == JFileChooser.APPROVE_OPTION) {
            val file = fileChooser.selectedFile
            try {
                val bytes = file.readBytes()
                onResult(bytes)
            } catch (e: Exception) {
                e.printStackTrace()
                onResult(null)
            }
        }
    }
}