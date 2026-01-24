package com.yolbertdev.coffeeplatform.presentation

import androidx.lifecycle.viewModelScope
import com.yolbertdev.coffeeplatform.domain.model.ClientDebt
import com.yolbertdev.coffeeplatform.domain.ports.PdfGenerator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope

// Estado de la UI
data class ReportUiState(
    val isLoading: Boolean = false,
    val pdfPath: String? = null, // Si tiene valor, mostramos el botón "Abrir"
    val error: String? = null
)

class ReportViewModel(
    private val pdfGenerator: PdfGenerator
) : ScreenModel {

    private val _uiState = MutableStateFlow(ReportUiState())
    val uiState: StateFlow<ReportUiState> = _uiState.asStateFlow()

    fun generateReport() {
        screenModelScope.launch {
            _uiState.value = ReportUiState(isLoading = true)

            // Simulación de datos (luego esto vendrá de tu DB)
            val data = listOf(
                ClientDebt("Juan Perez", 150.0, "2023-10-01"),
                ClientDebt("Maria Gomez", 200.50, "2023-10-05"),
                ClientDebt("Carlos Ruiz", 50.0, "2023-10-10")
            )

            try {
                val path = pdfGenerator.generateDebtReport(data)
                _uiState.value = ReportUiState(isLoading = false, pdfPath = path)
            } catch (e: Exception) {
                _uiState.value = ReportUiState(isLoading = false, error = e.message ?: "Error desconocido")
            }
        }
    }

    fun clearState() {
        _uiState.value = ReportUiState() // Reseteamos para generar otro
    }
}
