package com.yolbertdev.coffeeplatform.data.database.dao

import com.yolbertdev.coffeeplatform.db.CoffeeDatabase
import com.yolbertdev.coffeeplatform.db.PaymentQueries

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
}

