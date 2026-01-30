package com.yolbertdev.coffeeplatform.data.database.dao

import com.yolbertdev.coffeeplatform.db.CoffeeDatabase
import com.yolbertdev.coffeeplatform.db.PaymentQueries

class PaymentDao(
    private val database: CoffeeDatabase
) {
    private val queries = database.paymentQueries
    fun getPaymentsByCustomer(customerId: Long) =
        queries.selectByCustomerId(customerId).executeAsList()

    // 2. Reportes
    fun getPaymentsForReport() =
        queries.selectPaymentsForReport().executeAsList()

    // 3. Insertar Pago
    fun insert(
        amount: Double,
        payment_type_id: Long,
        customer_id: Long
    ) {
        queries.insert(
            amount = amount,
            payment_type_id = payment_type_id,
            customer_id = customer_id
        )
    }
}


