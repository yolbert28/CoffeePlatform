package com.yolbertdev.coffeeplatform.ui.main.screens.payment

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.yolbertdev.coffeeplatform.domain.ports.ReportRow
import com.yolbertdev.coffeeplatform.domain.repository.PaymentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PaymentTabUiState(
    val payments: List<ReportRow> = emptyList(),
    val isLoading: Boolean = false
)

class PaymentScreenModel(
    private val paymentRepository: PaymentRepository
) : ScreenModel {

    private val _state = MutableStateFlow(PaymentTabUiState())
    val state = _state.asStateFlow()

    // Esta función carga los datos reales de la BD
    fun loadPayments() {
        screenModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                // Usamos la consulta que ya teníamos para reportes, que trae nombres y fechas
                val result = paymentRepository.getPaymentsForReport()

                // Ordenamos por fecha descendente (lo más reciente primero) si no viene ordenado
                // Nota: col2 es la fecha formateada, idealmente ordenaríamos por timestamp en la query,
                // pero por ahora lo mostramos tal cual viene de la BD.
                _state.update { it.copy(payments = result, isLoading = false) }
            } catch (e: Exception) {
                e.printStackTrace()
                _state.update { it.copy(isLoading = false) }
            }
        }
    }
}