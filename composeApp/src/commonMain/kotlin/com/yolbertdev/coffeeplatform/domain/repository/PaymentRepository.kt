package com.yolbertdev.coffeeplatform.domain.repository

import com.yolbertdev.coffeeplatform.domain.model.Loan
import com.yolbertdev.coffeeplatform.domain.ports.ReportRow

interface PaymentRepository {
    suspend fun getPaymentsForReport(): List<ReportRow>
    suspend fun getPaymentsByCustomerId(customerId: Long): List<com.yolbertdev.coffeeplatform.domain.model.Payment>
    suspend fun insertPayment(payment: com.yolbertdev.coffeeplatform.domain.model.Payment, currentLoan: Loan)
    suspend fun getPendingLoans(): List<LoanWithCustomerName>

}
data class LoanWithCustomerName(
    val loan: Loan,
    val customerName: String,
    val currency: String
)