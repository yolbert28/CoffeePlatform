package com.yolbertdev.coffeeplatform.sync

import android.content.Context
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.yolbertdev.coffeeplatform.domain.ports.BackupFileManager
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class AndroidBackupFileManager(
    private val context: Context
) : BackupFileManager {

    // Continuaciones para suspender la corrutina hasta que el usuario elija el archivo
    private var importContinuation: ((String?) -> Unit)? = null
    private var exportContinuation: ((String?) -> Unit)? = null

    // Launchers de Activity Result
    private var filePickerLauncher: ActivityResultLauncher<Array<String>>? = null
    private var createDocumentLauncher: ActivityResultLauncher<String>? = null

    // Variable temporal para guardar el contenido mientras el usuario elige la ruta
    private var pendingExportContent: String? = null

    /**
     * Llama a este método en ComponentActivity.onCreate ANTES de setContent.
     */
    fun registerLauncher(activity: ComponentActivity) {
        // 1. Launcher para IMPORTAR (Abrir archivo existente)
        filePickerLauncher = activity.registerForActivityResult(
            ActivityResultContracts.OpenDocument()
        ) { uri: Uri? ->
            if (uri != null) {
                val content = readTextFromUri(uri)
                importContinuation?.invoke(content)
            } else {
                importContinuation?.invoke(null)
            }
            importContinuation = null
        }

        // 2. Launcher para EXPORTAR (Crear nuevo archivo) [NUEVO]
        createDocumentLauncher = activity.registerForActivityResult(
            ActivityResultContracts.CreateDocument("application/json")
        ) { uri: Uri? ->
            // Si uri no es null, el usuario eligió dónde guardar
            if (uri != null && pendingExportContent != null) {
                val success = writeTextToUri(uri, pendingExportContent!!)
                if (success) {
                    // Retornamos la URI (o un mensaje) para mostrar en la UI
                    exportContinuation?.invoke(uri.toString())
                } else {
                    exportContinuation?.invoke(null)
                }
            } else {
                // El usuario canceló o hubo error
                exportContinuation?.invoke(null)
            }
            // Limpieza
            exportContinuation = null
            pendingExportContent = null
        }
    }

    override suspend fun exportBackup(jsonContent: String, fileName: String): String? = suspendCancellableCoroutine { cont ->
        // Guardamos el contenido y la continuación
        pendingExportContent = jsonContent
        exportContinuation = { result -> cont.resume(result) }

        // Lanzamos el selector de sistema ("Guardar como...")
        // El usuario verá 'fileName' como nombre sugerido
        createDocumentLauncher?.launch(fileName) ?: cont.resume(null)
    }

    override suspend fun importBackup(): String? = suspendCancellableCoroutine { cont ->
        importContinuation = { content -> cont.resume(content) }
        // Solicitamos abrir archivos JSON o texto plano
        filePickerLauncher?.launch(arrayOf("application/json", "text/plain"))
            ?: cont.resume(null)
    }

    // --- Métodos Helper para leer/escribir usando URIs ---

    private fun readTextFromUri(uri: Uri): String? {
        return try {
            context.contentResolver.openInputStream(uri)?.bufferedReader()?.use { it.readText() }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun writeTextToUri(uri: Uri, content: String): Boolean {
        return try {
            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                outputStream.write(content.toByteArray(Charsets.UTF_8))
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}