package com.yolbertdev.coffeeplatform.data.database.dao

import com.yolbertdev.coffeeplatform.db.CoffeeDatabase

class PaymentDao(
    private val database: CoffeeDatabase
) {
    private val queries = database.paymentQueries

    fun getPaymentsForReport() = queries.selectPaymentsForReport().executeAsList()
}