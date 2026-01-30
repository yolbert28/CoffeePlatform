package com.yolbertdev.coffeeplatform.ui.main.screens.loan.add

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.yolbertdev.coffeeplatform.domain.model.Customer
import com.yolbertdev.coffeeplatform.domain.model.Loan
import com.yolbertdev.coffeeplatform.domain.usecase.InsertLoanUseCase
import com.yolbertdev.coffeeplatform.domain.usecase.SelectAllCustomerUseCase
import com.yolbertdev.coffeeplatform.util.DateMethods
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AddLoanUiState(
    val customers: List<Customer> = emptyList(),
    val selectedCustomer: Customer? = null,
    val amount: String = "",
    val interestRate: String = "10", // 10% por defecto
    val description: String = "",
    val selectedCurrencyIndex: Int = 0, // 0 = USD, 1 = QT
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false
)

class AddLoanScreenModel(
    private val insertLoanUseCase: InsertLoanUseCase,
    private val selectAllCustomerUseCase: SelectAllCustomerUseCase
) : ScreenModel {

    private val _uiState = MutableStateFlow(AddLoanUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadCustomers()
    }

    private fun loadCustomers() {
        screenModelScope.launch {
            try {
                // Obtenemos clientes para el Dropdown
                val customersList = selectAllCustomerUseCase()
                _uiState.update { it.copy(customers = customersList) }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun onCustomerSelected(customer: Customer) {
        _uiState.update { it.copy(selectedCustomer = customer) }
    }

    fun onAmountChanged(amount: String) {
        // Solo permitir números y un punto decimal
        if (amount.all { it.isDigit() || it == '.' }) {
            _uiState.update { it.copy(amount = amount) }
        }
    }

    fun onInterestChanged(interest: String) {
        // Permitimos decimales en el interés también por si acaso (ej: 10.5)
        if (interest.all { it.isDigit() || it == '.' }) {
            _uiState.update { it.copy(interestRate = interest) }
        }
    }

    fun onDescriptionChanged(desc: String) {
        _uiState.update { it.copy(description = desc) }
    }

    fun onCurrencyChanged(index: Int) {
        _uiState.update { it.copy(selectedCurrencyIndex = index) }
    }

    fun saveLoan(onSuccess: () -> Unit) {
        val state = _uiState.value
        // Validación básica
        if (state.selectedCustomer == null || state.amount.isBlank()) return

        screenModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val paymentTypeString = if (state.selectedCurrencyIndex == 0) "USD" else "QT"

                // Tiempos en milisegundos
                val currentTime = DateMethods.getCurrentTimeMillis()
                val paymentTime = currentTime + 2592000000L // +30 días

                val loan = Loan(
                    id = 0,
                    customerId = state.selectedCustomer.id.toInt(),
                    interestRate = state.interestRate.toDoubleOrNull() ?: 0.0,
                    description = state.description,
                    paymentDate = paymentTime, // Pasamos el Long directo
                    paymentType = paymentTypeString,
                    quantity = state.amount.toDoubleOrNull() ?: 0.0,
                    paid = 0.0,
                    statusId = 1,
                    creationDate = currentTime, // Pasamos el Long directo
                    updateDate = currentTime    // Pasamos el Long directo
                )

                insertLoanUseCase(loan)
                onSuccess()
            } catch (e: Exception) {
                e.printStackTrace()
                // Aquí podrías actualizar un estado de error si quisieras mostrarlo en UI
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}