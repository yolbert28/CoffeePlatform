package com.yolbertdev.coffeeplatform.domain.usecase

import com.yolbertdev.coffeeplatform.domain.model.BackupData
import com.yolbertdev.coffeeplatform.domain.ports.BackupFileManager
import com.yolbertdev.coffeeplatform.domain.repository.SyncRepository
import com.yolbertdev.coffeeplatform.util.ImageStorage
import kotlinx.serialization.json.Json
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class ImportBackupUseCase(
    private val syncRepository: SyncRepository,
    private val backupFileManager: BackupFileManager,
    private val imageStorage: ImageStorage
) {
    @OptIn(ExperimentalEncodingApi::class)
    suspend operator fun invoke(): Boolean {
        // 1. Obtener el contenido JSON del archivo seleccionado
        val jsonContent = backupFileManager.importBackup() ?: return false

        return try {
            val json = Json { ignoreUnknownKeys = true }
            val backupData: BackupData = json.decodeFromString(jsonContent)

            // 2. Restaurar las imágenes físicamente
            // Creamos un mapa: "nombre_archivo_original.jpg" -> "ruta_nueva_en_este_celular"
            val pathMapping = mutableMapOf<String, String>()

            backupData.images.forEach { (fileName, base64Data) ->
                try {
                    val bytes = Base64.decode(base64Data)
                    // Guardamos el archivo físico y obtenemos su nueva ruta absoluta
                    val newPath = imageStorage.saveImage(bytes)
                    if (newPath != null) {
                        pathMapping[fileName] = newPath
                    }
                } catch (e: Exception) {
                    println("Error restaurando imagen $fileName: ${e.message}")
                }
            }

            // 3. Actualizar las rutas en la lista de clientes
            val updatedCustomers = backupData.customers.map { customer ->
                if (!customer.photo.isNullOrBlank()) {
                    // Extraemos el nombre del archivo original (ej: "foto.jpg" de "/data/.../foto.jpg")
                    val originalFileName = customer.photo.substringAfterLast("/")

                    // Buscamos si tenemos una nueva ruta para esa foto
                    val newPath = pathMapping[originalFileName]

                    if (newPath != null) {
                        // Actualizamos el cliente con la nueva ruta válida
                        customer.copy(photo = newPath)
                    } else {
                        // Si falló la imagen, la dejamos nula para evitar rutas rotas
                        customer.copy(photo = null)
                    }
                } else {
                    customer
                }
            }

            // 4. Crear una copia de los datos con los clientes actualizados
            val finalBackupData = backupData.copy(customers = updatedCustomers)

            // 5. Insertar todo en la base de datos
            syncRepository.importAllData(finalBackupData)
            true

        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}