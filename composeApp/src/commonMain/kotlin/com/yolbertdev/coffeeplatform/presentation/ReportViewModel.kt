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
import com.yolbertdev.coffeeplatform.domain.ports.ReportRow
import com.yolbertdev.coffeeplatform.domain.ports.ReportType

data class ReportUiState(
    val isLoading: Boolean = false,
    val pdfPath: String? = null,
    val error: String? = null,

    // Filtros seleccionados
    val selectedType: ReportType = ReportType.DEBTORS,
    val selectedMonth: String = "Todos",
    val selectedCurrency: String = "Todos" // "Todos", "USD", "QT"
)

class ReportViewModel(
    private val pdfGenerator: PdfGenerator
) : ScreenModel {

    private val _uiState = MutableStateFlow(ReportUiState())
    val uiState: StateFlow<ReportUiState> = _uiState.asStateFlow()

    // Opciones para los filtros
    val months = listOf("Todos", "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre")
    val currencies = listOf("Todos", "USD", "QT")

    // --- Métodos de Selección ---
    fun selectReportType(type: ReportType) {
        _uiState.value = _uiState.value.copy(selectedType = type, pdfPath = null)
    }

    fun selectMonth(month: String) {
        _uiState.value = _uiState.value.copy(selectedMonth = month, pdfPath = null)
    }

    fun selectCurrency(currency: String) {
        _uiState.value = _uiState.value.copy(selectedCurrency = currency, pdfPath = null)
    }

    // --- Generación ---
    fun generateReport() {
        screenModelScope.launch {
            val currentState = _uiState.value
            _uiState.value = currentState.copy(isLoading = true, error = null, pdfPath = null)

            try {
                // 1. Obtener datos (Simulación de DB)
                val rawData = getMockData(currentState.selectedType)

                // 2. Aplicar Filtros (Lógica de Negocio)
                val filteredData = rawData.filter { row ->
                    // Filtro de Mes (Simulado chequeando si el string de fecha contiene algo, simplificado)
                    val monthMatch = if (currentState.selectedMonth == "Todos") true else {
                        // En un caso real, parsearías la fecha. Aquí simulo por convención.
                        // Para el ejemplo, asumiremos que todos pasan o crearé datos que coincidan.
                        true
                    }
                    // Filtro de Moneda
                    val currencyMatch = if (currentState.selectedCurrency == "Todos") true else {
                        row.col4 == currentState.selectedCurrency
                    }
                    monthMatch && currencyMatch
                }

                if (filteredData.isEmpty()) {
                    _uiState.value = currentState.copy(isLoading = false, error = "No hay datos con estos filtros")
                    return@launch
                }

                // 3. Generar PDF
                val filterDesc = "Filtro: Mes ${currentState.selectedMonth} - Moneda: ${currentState.selectedCurrency}"

                val path = pdfGenerator.generateReport(
                    type = currentState.selectedType,
                    data = filteredData,
                    filterDescription = filterDesc
                )

                _uiState.value = currentState.copy(isLoading = false, pdfPath = path)
            } catch (e: Exception) {
                _uiState.value = currentState.copy(isLoading = false, error = e.message ?: "Error desconocido")
            }
        }
    }

    // Datos Mock (Simulando lo que vendría de tu DB SQLDelight)
    private fun getMockData(type: ReportType): List<ReportRow> {
        return when (type) {
            ReportType.DEBTORS -> listOf(
                ReportRow("Juan Pérez", "2024-01-15", "150.00", "USD"),
                ReportRow("Maria Gomez", "2024-02-10", "12.5", "QT"), // Quintales
                ReportRow("Carlos Ruiz", "2024-02-15", "50.00", "USD"),
                ReportRow("Ana Lopez", "2024-03-01", "5.0", "QT")
            )
            ReportType.PAYMENTS -> listOf(
                ReportRow("Juan Pérez", "2024-01-20", "50.00", "USD"),
                ReportRow("Maria Gomez", "2024-02-12", "2.0", "QT")
            )
            ReportType.LOANS -> listOf(
                ReportRow("Pedro Pascal", "2024-01-05", "5000.00", "USD"),
                ReportRow("Elon Musk", "2024-01-10", "100.0", "QT")
            )
        }
    }
}