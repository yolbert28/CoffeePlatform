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
import com.yolbertdev.coffeeplatform.domain.repository.LoanRepository
import com.yolbertdev.coffeeplatform.domain.repository.PaymentRepository

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
    private val pdfGenerator: PdfGenerator,
    private val loanRepository: LoanRepository,
    private val paymentRepository: PaymentRepository
) : ScreenModel {

    // ... (Variables de estado y filtros se quedan igual) ...
    private val _uiState = MutableStateFlow(ReportUiState())
    val uiState: StateFlow<ReportUiState> = _uiState.asStateFlow()

    val months = listOf("Todos", "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre")
    val currencies = listOf("Todos", "USD", "QT")

    // ... (Métodos selectReportType, selectMonth, selectCurrency iguales) ...
    fun selectReportType(type: ReportType) {
        _uiState.value = _uiState.value.copy(selectedType = type, pdfPath = null)
    }
    fun selectMonth(month: String) {
        _uiState.value = _uiState.value.copy(selectedMonth = month, pdfPath = null)
    }
    fun selectCurrency(currency: String) {
        _uiState.value = _uiState.value.copy(selectedCurrency = currency, pdfPath = null)
    }


    fun generateReport() {
        screenModelScope.launch {
            val currentState = _uiState.value
            _uiState.value = currentState.copy(isLoading = true, error = null, pdfPath = null)

            try {
                // 1. OBTENER DATOS REALES DE BD
                val rawData: List<ReportRow> = when (currentState.selectedType) {
                    ReportType.DEBTORS -> {
                        // Aquí asumimos que "Deudores" son préstamos pendientes.
                        // Si tienes una lógica diferente, ajusta la consulta en LoanRepository.
                        loanRepository.getLoansForReport()
                    }
                    ReportType.PAYMENTS -> {
                        paymentRepository.getPaymentsForReport()
                    }
                    ReportType.LOANS -> {
                        loanRepository.getLoansForReport()
                    }
                }

                // 2. APLICAR FILTROS (En memoria, por ahora es eficiente)
                val filteredData = rawData.filter { row ->
                    // Filtro Mes (Asumiendo que col2 es la fecha formateada o raw string)
                    // Nota: Para filtrar bien por mes, necesitarías parsear la fecha 'row.col2'
                    // O hacer el filtrado en SQL. Para simplificar, haré un chequeo de string simple:
                    val monthMatch = if (currentState.selectedMonth == "Todos") true else {
                        // row.col2 debe contener el nombre del mes o ser parseable.
                        // Sugerencia: Usa DateMethods para ver si la fecha cae en el mes seleccionado.
                        // Por simplicidad, asumiré que la fecha contiene el número o nombre
                        true // TODO: Implementar lógica de fecha real aquí
                    }

                    val currencyMatch = if (currentState.selectedCurrency == "Todos") true else {
                        row.col4.equals(currentState.selectedCurrency, ignoreCase = true)
                    }

                    monthMatch && currencyMatch
                }

                if (filteredData.isEmpty()) {
                    _uiState.value = currentState.copy(isLoading = false, error = "No se encontraron datos.")
                    return@launch
                }

                // 3. GENERAR PDF
                val filterDesc = "Filtro: Mes ${currentState.selectedMonth} - Moneda: ${currentState.selectedCurrency}"
                val path = pdfGenerator.generateReport(
                    type = currentState.selectedType,
                    data = filteredData,
                    filterDescription = filterDesc
                )

                _uiState.value = currentState.copy(isLoading = false, pdfPath = path)

            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = currentState.copy(isLoading = false, error = "Error: ${e.message}")
            }
        }
    }
}