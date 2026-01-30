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
    val interestRate: String = "10",
    val description: String = "",
    val selectedCurrencyIndex: Int = 0,
    val paymentDate: Long = DateMethods.getCurrentTimeMillis() + 2592000000L,
    val isLoading: Boolean = false,

    // Nuevos campos para validación y feedback
    val amountError: String? = null,
    val customerError: String? = null,
    val showSuccessMessage: Boolean = false
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
                val customersList = selectAllCustomerUseCase()
                _uiState.update { it.copy(customers = customersList) }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun onCustomerSelected(customer: Customer) {
        _uiState.update { it.copy(selectedCustomer = customer, customerError = null) }
    }

    fun onAmountChanged(amount: String) {
        if (amount.all { it.isDigit() || it == '.' }) {
            _uiState.update { it.copy(amount = amount, amountError = null) }
        }
    }

    fun onInterestChanged(interest: String) {
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
    fun onPaymentDateChanged(dateMillis: Long) {
        _uiState.update { it.copy(paymentDate = dateMillis) }
    }
    fun saveLoan() {
        val state = _uiState.value
        var hasError = false

        // 1. Validaciones
        if (state.selectedCustomer == null) {
            _uiState.update { it.copy(customerError = "Debes seleccionar un cliente") }
            hasError = true
        }

        val amountValue = state.amount.toDoubleOrNull()
        if (amountValue == null || amountValue <= 0.0) {
            _uiState.update { it.copy(amountError = "Ingresa un monto válido mayor a 0") }
            hasError = true
        }

        if (hasError) return

        // 2. Guardado
        screenModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val paymentTypeString = if (state.selectedCurrencyIndex == 0) "USD" else "QT"
                val currentTime = DateMethods.getCurrentTimeMillis()
                val paymentTime = currentTime + 2592000000L // +30 días

                val loan = Loan(
                    id = 0,
                    customerId = state.selectedCustomer!!.id.toInt(),
                    interestRate = state.interestRate.toDoubleOrNull() ?: 0.0,
                    description = state.description,
                    paymentDate = paymentTime,
                    paymentType = paymentTypeString,
                    quantity = amountValue!!,
                    paid = 0.0,
                    statusId = 1,
                    creationDate = currentTime,
                    updateDate = currentTime
                )

                insertLoanUseCase(loan)
                // Indicamos éxito para que la UI reaccione
                _uiState.update { it.copy(showSuccessMessage = true) }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    // Resetea el flag después de navegar
    fun onNavigationHandled() {
        _uiState.update { it.copy(showSuccessMessage = false) }
    }
}