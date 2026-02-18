package com.yolbertdev.coffeeplatform.domain.usecase

import com.yolbertdev.coffeeplatform.domain.model.BackupData
import com.yolbertdev.coffeeplatform.domain.ports.BackupFileManager
import com.yolbertdev.coffeeplatform.domain.repository.SyncRepository
import com.yolbertdev.coffeeplatform.util.ImageStorage
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class ExportBackupUseCase(
    private val syncRepository: SyncRepository,
    private val backupFileManager: BackupFileManager,
    private val imageStorage: ImageStorage
) {
    @OptIn(ExperimentalEncodingApi::class)
    suspend operator fun invoke(): String? {
        // 1. Obtener los datos básicos de la base de datos
        val rawData: BackupData = syncRepository.exportAllData()

        // 2. Crear un mapa para guardar las imágenes (NombreArchivo -> Base64)
        val imagesMap = mutableMapOf<String, String>()

        // 3. Recorrer los clientes para procesar sus fotos
        rawData.customers.forEach { customer ->
            val photoPath = customer.photo
            // Verificamos que tenga ruta y que no esté vacía
            if (!photoPath.isNullOrBlank()) {
                // Leemos el archivo físico a bytes
                val bytes = imageStorage.readImage(photoPath)

                if (bytes != null) {
                    // Obtenemos solo el nombre del archivo (ej: "/data/img.jpg" -> "img.jpg")
                    // Esto sirve de clave para recuperarla luego
                    val fileName = photoPath.substringAfterLast("/")

                    // Codificamos a texto Base64
                    val base64String = Base64.encode(bytes)

                    // Lo guardamos en el mapa
                    imagesMap[fileName] = base64String
                }
            }
        }

        // 4. Creamos la versión final del backup incluyendo las imágenes
        // Asume que ya agregaste 'val images: Map<String, String>' a tu data class BackupData
        val finalBackupData = rawData.copy(images = imagesMap)

        // 5. Configuración del JSON
        val json = Json {
            prettyPrint = true
            encodeDefaults = true
        }

        // 6. Generar el String JSON final
        val jsonContent = json.encodeToString(finalBackupData)

        // 7. Definir nombre y guardar
        val fileName = "coffeeplatform_backup_${finalBackupData.exportDate}.json"
        return backupFileManager.exportBackup(jsonContent, fileName)
    }
}