package com.yolbertdev.coffeeplatform.domain.repository

import com.yolbertdev.coffeeplatform.domain.model.Loan
import com.yolbertdev.coffeeplatform.domain.model.Payment
import com.yolbertdev.coffeeplatform.domain.ports.ReportRow
import kotlinx.coroutines.flow.Flow

// Clase auxiliar para mostrar en el Dropdown
data class LoanWithCustomerName(
    val loan: Loan,
    val customerName: String,
    val currency: String
)

interface PaymentRepository {
    // Inserta el pago y actualiza el saldo del préstamo
    suspend fun insertPayment(payment: Payment, currentLoan: Loan)

    // Obtiene lista para reportes
    suspend fun getPaymentsForReport(): List<ReportRow>

    // Nuevo método para la UI que incluye detalles del cliente
    fun getPaymentsWithDetails(): Flow<List<Payment>>

    // Obtiene pagos por cliente
    suspend fun getPaymentsByCustomerId(customerId: Long): List<Payment>

    // Obtiene préstamos pendientes para llenar el Dropdown de "Agregar Pago"
    suspend fun getPendingLoans(): List<LoanWithCustomerName>
}