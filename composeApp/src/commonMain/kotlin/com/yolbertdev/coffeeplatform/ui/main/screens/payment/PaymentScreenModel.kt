package com.yolbertdev.coffeeplatform.ui.main.screens.payment

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.yolbertdev.coffeeplatform.domain.ports.ReportRow
import com.yolbertdev.coffeeplatform.domain.repository.PaymentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PaymentListState(
    val isLoading: Boolean = false,
    val payments: List<ReportRow> = emptyList(),
    val error: String? = null
)

class PaymentScreenModel(
    private val repository: PaymentRepository
) : ScreenModel {

    private val _state = MutableStateFlow(PaymentListState())
    val state = _state.asStateFlow()

    fun loadPayments() {
        screenModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                // Usamos la consulta que ya hace los JOINs necesarios
                val result = repository.getPaymentsForReport()
                _state.update {
                    it.copy(isLoading = false, payments = result)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _state.update {
                    it.copy(isLoading = false, error = e.message)
                }
            }
        }
    }
}