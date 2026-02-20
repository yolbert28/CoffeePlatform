package com.yolbertdev.coffeeplatform.ui.main.screens.payment.add

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.yolbertdev.coffeeplatform.domain.model.Payment
import com.yolbertdev.coffeeplatform.domain.repository.LoanWithCustomerName
import com.yolbertdev.coffeeplatform.domain.usecase.CreatePaymentUseCase
import com.yolbertdev.coffeeplatform.util.DateMethods
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AddPaymentUiState(
    val pendingLoans: List<LoanWithCustomerName> = emptyList(),
    val selectedLoanWrapper: LoanWithCustomerName? = null,
    val amount: String = "",
    val paymentDate: Long = DateMethods.getCurrentTimeMillis(),
    val note: String = "",
    val remainingAmount: Double = 0.0,
    val currency: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
)

class AddPaymentScreenModel(
    private val createPaymentUseCase: CreatePaymentUseCase
) : ScreenModel {

    private val _state = MutableStateFlow(AddPaymentUiState())
    val state = _state.asStateFlow()

    init {
        loadPendingLoans()
    }

    private fun loadPendingLoans() {
        screenModelScope.launch {
            try {
                val loans = createPaymentUseCase.getPendingLoans()
                _state.update { it.copy(pendingLoans = loans) }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun onLoanSelected(wrapper: LoanWithCustomerName) {
        val loan = wrapper.loan
        // Calculamos deuda restante: Total - Pagado
        val remaining = loan.quantity - loan.paid

        _state.update {
            it.copy(
                selectedLoanWrapper = wrapper,
                remainingAmount = remaining,
                currency = wrapper.currency,
                amount = "", // Resetear monto al cambiar de préstamo
                error = null
            )
        }
    }

    fun onAmountChanged(v: String) {
        // Solo permitir números y un punto decimal
        if (v.all { it.isDigit() || it == '.' }) {
            _state.update { it.copy(amount = v, error = null) }
        }
    }

    fun onDateChanged(date: Long) {
        _state.update { it.copy(paymentDate = date) }
    }

    fun onNoteChanged(v: String) {
        _state.update { it.copy(note = v) }
    }

    fun savePayment(onSuccess: () -> Unit) {
        val s = _state.value

        if (s.selectedLoanWrapper == null) {
            _state.update { it.copy(error = "Selecciona un préstamo primero") }
            return
        }

        val amountVal = s.amount.toDoubleOrNull()
        if (amountVal == null || amountVal <= 0.0) {
            _state.update { it.copy(error = "Ingresa un monto válido") }
            return
        }

        // Validación simple: no pagar más de lo que se debe (+ margen de error decimal)
        if (amountVal > s.remainingAmount + 0.05) {
            _state.update { it.copy(error = "El monto excede la deuda (${s.remainingAmount})") }
            return
        }

        // NUEVA VALIDACIÓN: La fecha del pago no puede ser anterior a la del préstamo
        // Restamos 24h (86_400_000 ms) para asegurar que permita registrar el pago el mismo día del préstamo.
        val loanCreationDate = s.selectedLoanWrapper.loan.creationDate
        if (s.paymentDate < (loanCreationDate - 86_400_000L)) {
            _state.update { it.copy(error = "La fecha del pago no puede ser anterior al préstamo") }
            return
        }

        screenModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val payment = Payment(
                    id = 0,
                    loanId = s.selectedLoanWrapper.loan.id,
                    amount = amountVal,
                    creationDate = s.paymentDate,
                    note = s.note,
                    // Estos datos vienen del préstamo seleccionado
                    paymentType = s.selectedLoanWrapper.loan.paymentType,
                    customerId = s.selectedLoanWrapper.loan.customerId,
                    updateDate = DateMethods.getCurrentTimeMillis()
                )

                createPaymentUseCase(payment, s.selectedLoanWrapper.loan)
                _state.update { it.copy(success = true, isLoading = false) }
                onSuccess()
            } catch (e: Exception) {
                e.printStackTrace()
                _state.update { it.copy(isLoading = false, error = "Error al guardar: ${e.message}") }
            }
        }
    }

    fun onHandled() {
        _state.update { it.copy(success = false) }
    }
}