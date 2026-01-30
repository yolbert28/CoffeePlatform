package com.yolbertdev.coffeeplatform.data.database.repository

import com.yolbertdev.coffeeplatform.data.database.dao.PaymentDao
import com.yolbertdev.coffeeplatform.db.CoffeeDatabase
import com.yolbertdev.coffeeplatform.domain.model.Payment
import com.yolbertdev.coffeeplatform.domain.repository.PaymentRepository
import com.yolbertdev.coffeeplatform.domain.ports.ReportRow
import com.yolbertdev.coffeeplatform.util.DateMethods

class PaymentRepositoryImpl(
    private val db: CoffeeDatabase,
    private val dao: PaymentDao
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

    override suspend fun getPaymentsByCustomerId(customerId: Long): List<Payment> {
        return dao.getPaymentsByCustomer(customerId).map {
            Payment(
                id = it.id,
                amount = it.amount,
                // Lógica de transformación DB -> Dominio
                paymentType = if (it.payment_type_id == 1L) "USD" else "QT",
                customerId = it.customer_id,
                creationDate = it.creation_date,
                updateDate = it.update_date
            )
        }
    }
    }