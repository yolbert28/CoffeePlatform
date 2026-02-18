package com.yolbertdev.coffeeplatform.data.database.dao

import com.yolbertdev.coffeeplatform.db.CoffeeDatabase
import com.yolbertdev.coffeeplatform.db.Customer as CustomerDb
import com.yolbertdev.coffeeplatform.db.Loan as LoanDb
import com.yolbertdev.coffeeplatform.db.Payment as PaymentDb

class SyncDao(private val database: CoffeeDatabase) {

    // ───────── EXPORT ─────────

    fun selectAllCustomers(): List<CustomerDb> =
        database.customerQueries.selectAll().executeAsList()

    fun selectAllLoans(): List<LoanDb> =
        database.loanQueries.selectAllWithCustomer().executeAsList().map { row ->
            LoanDb(
                id = row.id,
                customer_id = row.customer_id,
                interest_rate = row.interest_rate,
                payment_type_id = row.payment_type_id,
                amount = row.amount,
                payment_date = row.payment_date,
                paid = row.paid,
                description = row.description,
                creation_date = row.creation_date,
                update_date = row.update_date,
                status_id = row.status_id
            )
        }.distinctBy { it.id }

    fun selectAllPayments(): List<PaymentDb> =
        database.paymentQueries.selectWithDetails().executeAsList().map { row ->
            PaymentDb(
                id = row.payment_id,
                loan_id = row.loan_id,
                amount = row.amount,
                payment_date = row.payment_date,
                note = row.note,
                creation_date = row.payment_update_date,
                update_date = row.payment_update_date
            )
        }.distinctBy { it.id }

    // ───────── IMPORT ─────────

    fun customerExists(id: Long): Boolean =
        database.customerQueries.selectById(id).executeAsOneOrNull() != null

    fun loanExistsById(id: Long): Boolean =
        database.loanQueries.selectByCustomerId(id.toLong()).executeAsList()
            .any { it.id == id }

    fun insertCustomerRaw(
        idCard: String?, name: String, nickname: String, description: String,
        creditLevel: Long, location: String, photo: String, statusId: Long
    ) {
        database.customerQueries.insert(
            id_card = idCard,
            name = name,
            nickname = nickname,
            description = description,
            credit_level = creditLevel,
            location = location,
            photo = photo,
            status_id = statusId
        )
    }

    fun insertLoanRaw(
        customerId: Long, interestRate: Double, paymentTypeId: Long,
        amount: Double, paymentDate: Long, paid: Double, description: String,
        statusId: Long, creationDate: Long, updateDate: Long
    ) {
        database.loanQueries.insert(
            customerId,
            interestRate,
            paymentTypeId,
            amount,
            paymentDate,
            paid,
            description,
            statusId,
            creationDate,
            updateDate
        )
    }

    fun insertPaymentRaw(
        loanId: Long, amount: Double, paymentDate: Long, note: String?
    ) {
        database.paymentQueries.insert(
            loanId = loanId,
            amount = amount,
            paymentDate = paymentDate,
            note = note
        )
    }
}