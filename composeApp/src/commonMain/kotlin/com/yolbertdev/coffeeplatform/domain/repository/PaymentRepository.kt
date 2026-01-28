package com.yolbertdev.coffeeplatform.domain.repository

import com.yolbertdev.coffeeplatform.domain.ports.ReportRow

interface PaymentRepository {
    suspend fun getPaymentsForReport(): List<ReportRow>
}