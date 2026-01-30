package com.yolbertdev.coffeeplatform.ui.main.screens.customer.detail

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.yolbertdev.coffeeplatform.domain.model.Customer
import com.yolbertdev.coffeeplatform.domain.model.Loan
import com.yolbertdev.coffeeplatform.domain.repository.LoanRepository
// import com.yolbertdev.coffeeplatform.domain.repository.PaymentRepository // Descomenta cuando tengas PaymentRepository listo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CustomerDetailUiState(
    val isLoading: Boolean = false,
    val loans: List<Loan> = emptyList(),
    // val payments: List<Payment> = emptyList() // TODO: Agregar modelo Payment
)

class CustomerDetailScreenModel(
    private val loanRepository: LoanRepository,
    // private val paymentRepository: PaymentRepository
) : ScreenModel {

    private val _uiState = MutableStateFlow(CustomerDetailUiState())
    val uiState = _uiState.asStateFlow()

    fun loadData(customerId: Long) {
        screenModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val loans = loanRepository.getLoansByCustomerId(customerId)
                // val payments = paymentRepository.getPaymentsByCustomerId(customerId)

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        loans = loans,
                        // payments = payments
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}