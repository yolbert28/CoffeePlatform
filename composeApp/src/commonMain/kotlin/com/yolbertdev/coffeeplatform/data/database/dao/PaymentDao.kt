package com.yolbertdev.coffeeplatform.data.database.dao

import com.yolbertdev.coffeeplatform.db.CoffeeDatabase

class PaymentDao(
    private val database: CoffeeDatabase
) {
    private val queries = database.paymentQueries

    fun getPaymentsByCustomer(customerId: Long) =
        queries.selectByCustomerId(customerId).executeAsList()

    // Esta función devolverá ahora una lista más simple
    fun getPaymentsForReport() =
        queries.selectPaymentsForReport().executeAsList()

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
}