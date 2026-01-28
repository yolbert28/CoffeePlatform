package com.yolbertdev.coffeeplatform.data.database.repository

import com.yolbertdev.coffeeplatform.db.CoffeeDatabase
import com.yolbertdev.coffeeplatform.domain.repository.PaymentRepository
import com.yolbertdev.coffeeplatform.domain.ports.ReportRow
import com.yolbertdev.coffeeplatform.util.DateMethods

class PaymentRepositoryImpl(
    private val db: CoffeeDatabase
) : PaymentRepository {
    override suspend fun getPaymentsForReport(): List<ReportRow> {
        return db.paymentQueries.selectPaymentsForReport().executeAsList().map {
            ReportRow(
                col1 = it.clientName,
                col2 = DateMethods.formatDate(it.creation_date),
                col3 = it.amount.toString(),
                col4 = it.currencyName ?: "N/A"
            )
        }
    }
}