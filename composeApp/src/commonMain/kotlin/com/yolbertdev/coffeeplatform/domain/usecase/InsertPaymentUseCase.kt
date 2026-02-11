package com.yolbertdev.coffeeplatform.domain.usecase

import com.yolbertdev.coffeeplatform.domain.model.Loan
import com.yolbertdev.coffeeplatform.domain.model.Payment
import com.yolbertdev.coffeeplatform.domain.repository.LoanWithCustomerName
import com.yolbertdev.coffeeplatform.domain.repository.PaymentRepository

class CreatePaymentUseCase(private val repository: PaymentRepository) {

    // Obtiene la lista de préstamos que aún no se han pagado (para el dropdown)
    suspend fun getPendingLoans(): List<LoanWithCustomerName> {
        return repository.getPendingLoans()
    }

    // Registra el pago y ejecuta la lógica de negocio (actualizar saldo del préstamo)
    suspend operator fun invoke(payment: Payment, currentLoan: Loan) {
        repository.insertPayment(payment, currentLoan)
    }
}