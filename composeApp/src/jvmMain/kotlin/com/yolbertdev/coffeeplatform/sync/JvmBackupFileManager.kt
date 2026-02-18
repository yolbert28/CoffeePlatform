package com.yolbertdev.coffeeplatform.sync

import com.yolbertdev.coffeeplatform.domain.ports.BackupFileManager
import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import java.io.FilenameFilter

/**
 * Implementación Desktop (JVM) del [BackupFileManager].
 * Usa [java.awt.FileDialog] para el selector nativo del sistema operativo,
 * compatible con Windows, macOS y Linux.
 */
class JvmBackupFileManager : BackupFileManager {

    override suspend fun exportBackup(jsonContent: String, fileName: String): String? {
        return try {
            val dialog = FileDialog(null as Frame?, "Guardar Backup", FileDialog.SAVE).apply {
                this.file = fileName
                filenameFilter = FilenameFilter { _, name -> name.endsWith(".json") }
                isVisible = true
            }

            val directory = dialog.directory ?: return null
            val selectedFile = dialog.file ?: return null

            val outputFile = File(directory, selectedFile)
            outputFile.writeText(jsonContent, Charsets.UTF_8)
            outputFile.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun importBackup(): String? {
        return try {
            val dialog = FileDialog(null as Frame?, "Seleccionar Backup", FileDialog.LOAD).apply {
                filenameFilter = FilenameFilter { _, name -> name.endsWith(".json") }
                isVisible = true
            }

            val directory = dialog.directory ?: return null
            val selectedFile = dialog.file ?: return null

            val inputFile = File(directory, selectedFile)
            if (!inputFile.exists()) return null
            inputFile.readText(Charsets.UTF_8)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
