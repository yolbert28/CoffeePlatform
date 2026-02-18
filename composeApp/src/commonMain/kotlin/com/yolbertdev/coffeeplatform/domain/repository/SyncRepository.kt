package com.yolbertdev.coffeeplatform.domain.repository
import com.yolbertdev.coffeeplatform.domain.model.BackupData

interface SyncRepository {
    /**
     * Extrae todos los datos de la base de datos y los empaqueta en un BackupData.
     */
    suspend fun exportAllData(): BackupData

    /**
     * Recibe un BackupData y restaura los datos en la base de datos local.
     * Realiza un merge inteligente: no duplica registros que ya existen.
     */
    suspend fun importAllData(backupData: BackupData)
}