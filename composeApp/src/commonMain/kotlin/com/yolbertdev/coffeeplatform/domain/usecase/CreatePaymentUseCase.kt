package com.yolbertdev.coffeeplatform.domain.usecase

import com.yolbertdev.coffeeplatform.domain.model.Loan
import com.yolbertdev.coffeeplatform.domain.model.Payment
import com.yolbertdev.coffeeplatform.domain.repository.PaymentRepository

class CreatePaymentUseCase(private val repository: PaymentRepository) {

    suspend fun getPendingLoans() = repository.getPendingLoans()

    suspend operator fun invoke(payment: Payment, currentLoan: Loan) {
        repository.insertPayment(payment, currentLoan)
    }
}