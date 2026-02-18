package com.yolbertdev.coffeeplatform.domain.ports

interface BackupFileManager {
    /**
     * Exporta el JSON del backup a un archivo .json en el dispositivo.
     * Retorna la ruta del archivo generado, o null si falló.
     */
    suspend fun exportBackup(jsonContent: String, fileName: String): String?

    /**
     * Abre un selector de archivos para que el usuario elija un .json de backup.
     * Retorna el contenido del archivo como String, o null si el usuario canceló.
     */
    suspend fun importBackup(): String?
}