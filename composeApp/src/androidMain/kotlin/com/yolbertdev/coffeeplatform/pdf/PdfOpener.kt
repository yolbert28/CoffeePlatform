package com.yolbertdev.coffeeplatform.pdf

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import java.io.File

fun openPdf(context: Context, filePath: String) {
    val file = File(filePath)

    // Validamos que exista para no crashear
    if (!file.exists()) return

    // Generamos la URI segura usando la misma authority del Manifest
    // Nota: BuildConfig.APPLICATION_ID suele ser segura, o escribe "com.yolbertdev.coffeeplatform.provider" manualmente
    val uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        file
    )

    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(uri, "application/pdf")
        // Banderas cruciales para dar permiso temporal a la app de PDF
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // Necesario si lanzas desde fuera de una Activity
    }

    // Usamos createChooser para que el usuario elija su visor favorito (Drive, Adobe, etc)
    val chooser = Intent.createChooser(intent, "Abrir reporte con...")
    chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // Importante para KMP si usas contexto de App

    context.startActivity(chooser)
}