package com.yolbertdev.coffeeplatform.ui.main.screens.customer.detail

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.yolbertdev.coffeeplatform.domain.model.Customer
import com.yolbertdev.coffeeplatform.domain.model.Loan
import com.yolbertdev.coffeeplatform.domain.model.Payment
import com.yolbertdev.coffeeplatform.domain.repository.LoanRepository
import com.yolbertdev.coffeeplatform.domain.repository.PaymentRepository
import com.yolbertdev.coffeeplatform.domain.usecase.GetCustomerByIdUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class CustomerDetailUiState(
    val isLoading: Boolean = false,
    val customer: Customer? = null,
    val loans: List<Loan> = emptyList(),
    val payments: List<Payment> = emptyList(),
)

class CustomerDetailScreenModel(
    private val getCustomerByIdUseCase: GetCustomerByIdUseCase,
    private val loanRepository: LoanRepository,
    private val paymentRepository: PaymentRepository
) : ScreenModel {

    private val _uiState = MutableStateFlow(CustomerDetailUiState())
    val uiState = _uiState.asStateFlow()
    
    private var dataJob: Job? = null

    fun loadData(customerId: Long) {
        dataJob?.cancel() // Cancelamos cualquier carga anterior inmediatamente
        
        _uiState.update { CustomerDetailUiState(isLoading = true) } // Reseteamos el estado a cargando
        
        dataJob = screenModelScope.launch {
            // 1. Carga de datos del cliente (Reactivo)
            val customerFlow = getCustomerByIdUseCase(customerId.toInt())
            
            // 2. Carga de préstamos y pagos (Estático)
            try {
                val loans = loanRepository.getLoansByCustomerId(customerId)
                val payments = paymentRepository.getPaymentsByCustomerId(customerId)
                
                // Combinamos la suscripción del cliente con la carga de los otros datos
                customerFlow.collect { customer ->
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            customer = customer,
                            loans = loans,
                            payments = payments
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}