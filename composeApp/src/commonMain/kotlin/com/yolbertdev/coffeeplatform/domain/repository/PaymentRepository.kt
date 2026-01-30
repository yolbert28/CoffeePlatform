package com.yolbertdev.coffeeplatform.domain.repository

import com.yolbertdev.coffeeplatform.db.Payment
import com.yolbertdev.coffeeplatform.domain.ports.ReportRow

interface PaymentRepository {
    suspend fun getPaymentsForReport(): List<ReportRow>
    suspend fun getPaymentsByCustomerId(customerId: Long): List<com.yolbertdev.coffeeplatform.domain.model.Payment>

}