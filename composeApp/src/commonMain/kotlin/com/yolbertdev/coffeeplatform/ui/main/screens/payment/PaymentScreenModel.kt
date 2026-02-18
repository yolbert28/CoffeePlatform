package com.yolbertdev.coffeeplatform.ui.main.screens.payment

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.yolbertdev.coffeeplatform.domain.model.Payment
import com.yolbertdev.coffeeplatform.domain.repository.PaymentRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class PaymentListState(
    val isLoading: Boolean = false,
    val payments: List<Payment> = emptyList(),
    val error: String? = null
)

class PaymentScreenModel(
    private val repository: PaymentRepository
) : ScreenModel {

    private val _state = MutableStateFlow(PaymentListState())
    val state = _state.asStateFlow()

    init {
        loadPayments()
    }

    fun loadPayments() {
        screenModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            repository.getPaymentsWithDetails()
                .catch { e ->
                    _state.update { it.copy(isLoading = false, error = e.message) }
                }
                .collect { result ->
                    _state.update { it.copy(isLoading = false, payments = result) }
                }
        }
    }
}