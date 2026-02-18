package com.yolbertdev.coffeeplatform.domain.usecase

import com.yolbertdev.coffeeplatform.domain.model.BackupData
import com.yolbertdev.coffeeplatform.domain.ports.BackupFileManager
import com.yolbertdev.coffeeplatform.domain.repository.SyncRepository
import kotlinx.serialization.json.Json

class ImportBackupUseCase(
    private val syncRepository: SyncRepository,
    private val backupFileManager: BackupFileManager
) {
    suspend operator fun invoke(): Boolean {
        val jsonContent = backupFileManager.importBackup() ?: return false
        val json = Json { ignoreUnknownKeys = true }
        val backupData: BackupData = json.decodeFromString(jsonContent)
        syncRepository.importAllData(backupData)
        return true
    }
}