package com.yolbertdev.coffeeplatform.data.database.dao

import com.yolbertdev.coffeeplatform.db.CoffeeDatabase
import com.yolbertdev.coffeeplatform.domain.model.Customer
import com.yolbertdev.coffeeplatform.domain.model.Loan

class LoanDao(
    private val database: CoffeeDatabase
) {
    private val queries = database.loanQueries

    fun getLoansForReport() = queries.selectLoansForReport().executeAsList()

    // 1. INSERT: Ahora acepta Double en interest_rate y paid
    fun insert(
        customer_id: Long,
        interest_rate: Double, // Ahora coincide con SQL REAL
        payment_type_id: Long,
        amount: Double,
        payment_date: Long,
        paid: Double,          // Ahora coincide con SQL REAL
        description: String,
        status_id: Long
    ) {
        queries.insert(
            customer_id,
            interest_rate,
            payment_type_id,
            amount,
            payment_date,
            paid,
            description,
            status_id
        )
    }

    fun getLoansByCustomer(customerId: Long) = queries.selectByCustomerId(customerId).executeAsList()

    fun getAllLoansWithCustomer(): List<Pair<Loan, Customer>> {
        return queries.selectAllWithCustomer {
            // Parámetros de Loan (SQLDelight genera Double para los campos REAL)
                l_id, l_customerId, l_interestRate, l_paymentTypeId, l_amount, l_paymentDate, l_paid, l_desc, l_createDate, l_updateDate, l_statusId,
                // Parámetros de Customer
                c_id, c_idCard, c_name, c_nickname, c_desc, c_creditLevel, c_location, c_photo, c_createDate, c_updateDate, c_statusId ->

            val loan = Loan(
                id = l_id.toInt(),
                customerId = l_customerId.toInt(),
                interestRate = l_interestRate, // Ya viene como Double desde SQLDelight
                description = l_desc,
                paymentDate = l_paymentDate,
                paymentType = if (l_paymentTypeId == 1L) "USD" else "QT",
                quantity = l_amount,
                paid = l_paid, // Ya viene como Double
                statusId = l_statusId.toInt(),
                creationDate = l_createDate,
                updateDate = l_updateDate
            )

            val customer = Customer(
                id = c_id,
                idCard = c_idCard,
                name = c_name,
                nickname = c_nickname,
                description = c_desc,
                creditLevel = c_creditLevel,
                location = c_location,
                photo = c_photo,
                creationDate = c_createDate,
                updateDate = c_updateDate,
                statusId = c_statusId
            )

            Pair(loan, customer)
        }.executeAsList()
    }
}