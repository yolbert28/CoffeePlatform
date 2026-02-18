package com.yolbertdev.coffeeplatform.ui.main.screens.sync

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.yolbertdev.coffeeplatform.domain.usecase.ExportBackupUseCase
import com.yolbertdev.coffeeplatform.domain.usecase.ImportBackupUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SyncUiState(
    val isLoading: Boolean = false,
    val exportedFilePath: String? = null,
    val successMessage: String? = null,
    val errorMessage: String? = null
)

class SyncScreenModel(
    private val exportBackupUseCase: ExportBackupUseCase,
    private val importBackupUseCase: ImportBackupUseCase
) : ScreenModel {

    private val _uiState = MutableStateFlow(SyncUiState())
    val uiState = _uiState.asStateFlow()

    fun exportBackup() {
        screenModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null) }
            try {
                val filePath = exportBackupUseCase()
                if (filePath != null) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            exportedFilePath = filePath,
                            successMessage = "✅ Backup exportado correctamente"
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = "No se pudo guardar el archivo")
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = "Error al exportar: ${e.message}")
                }
            }
        }
    }

    fun importBackup() {
        screenModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null) }
            try {
                val imported = importBackupUseCase()
                if (imported) {
                    _uiState.update {
                        it.copy(isLoading = false, successMessage = "✅ Datos restaurados correctamente")
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false) }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = "Error al importar: ${e.message}")
                }
            }
        }
    }

    fun clearMessages() {
        _uiState.update { it.copy(successMessage = null, errorMessage = null) }
    }
}