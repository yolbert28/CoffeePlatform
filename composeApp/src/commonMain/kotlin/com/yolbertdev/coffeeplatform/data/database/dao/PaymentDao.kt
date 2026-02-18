package com.yolbertdev.coffeeplatform.data.database.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.yolbertdev.coffeeplatform.db.CoffeeDatabase
import com.yolbertdev.coffeeplatform.db.SelectWithDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow

class PaymentDao(
    private val database: CoffeeDatabase
) {
    private val queries = database.paymentQueries

    // Inserta un pago vinculado a un préstamo
    fun insert(
        loanId: Long,
        amount: Double,
        paymentDate: Long,
        note: String?
    ) {
        queries.insert(
            loanId = loanId,
            amount = amount,
            paymentDate = paymentDate,
            note = note
        )
    }

    // Obtiene pagos para el reporte general (con nombres)
    fun getPaymentsForReport() =
        queries.selectPaymentsForReport().executeAsList()

    // Obtiene pagos de un cliente específico
    fun getPaymentsByCustomer(customerId: Long) =
        queries.selectByCustomerId(customerId).executeAsList()

    // El DAO ahora solo devuelve el tipo generado por SQLDelight
    fun getPaymentsWithDetails(): Flow<List<SelectWithDetails>> {
        return queries.selectWithDetails()
            .asFlow()
            .mapToList(Dispatchers.IO)
    }
}