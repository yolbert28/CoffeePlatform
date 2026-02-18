package com.yolbertdev.coffeeplatform.domain.usecase

import com.yolbertdev.coffeeplatform.domain.model.BackupData
import com.yolbertdev.coffeeplatform.domain.ports.BackupFileManager
import com.yolbertdev.coffeeplatform.domain.repository.SyncRepository
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ExportBackupUseCase(
    private val syncRepository: SyncRepository,
    private val backupFileManager: BackupFileManager
) {
    suspend operator fun invoke(): String? {
        val backupData: BackupData = syncRepository.exportAllData()
        val json = Json {
            prettyPrint = true
            encodeDefaults = true
        }
        val jsonContent = json.encodeToString(backupData)
        val fileName = "coffeeplatform_backup_${backupData.exportDate}.json"
        return backupFileManager.exportBackup(jsonContent, fileName)
    }
}