package com.yolbertdev.coffeeplatform.data.database.repository

import com.yolbertdev.coffeeplatform.data.database.dao.PaymentDao
import com.yolbertdev.coffeeplatform.db.CoffeeDatabase
import com.yolbertdev.coffeeplatform.domain.model.Loan
import com.yolbertdev.coffeeplatform.domain.model.Payment
import com.yolbertdev.coffeeplatform.domain.repository.LoanWithCustomerName
import com.yolbertdev.coffeeplatform.domain.repository.PaymentRepository
import com.yolbertdev.coffeeplatform.domain.ports.ReportRow
import com.yolbertdev.coffeeplatform.util.DateMethods
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PaymentRepositoryImpl(
    private val db: CoffeeDatabase,
    private val dao: PaymentDao
) : PaymentRepository {

    override suspend fun getPaymentsForReport(): List<ReportRow> {
        // Ahora selectPaymentsForReport sí devuelve clientName y currencyName
        return db.paymentQueries.selectPaymentsForReport().executeAsList().map {
            ReportRow(
                col1 = it.clientName, // Esto ya no dará error
                col2 = DateMethods.formatDate(it.payment_date),
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
                // Usamos los campos que trajimos del JOIN con la tabla Loan
                paymentType = if (it.payment_type_id == 1L) "USD" else "QT",
                customerId = it.customer_id.toInt(),
                creationDate = it.payment_date,
                updateDate = it.update_date, // Ahora viene de la tabla Loan
                note = it.note,
                loanId = it.loan_id.toInt()
            )
        }
    }

    override suspend fun insertPayment(payment: Payment, currentLoan: Loan) = withContext(Dispatchers.IO) {
        db.transaction {
            dao.insert(
                loanId = payment.loanId.toLong(),
                amount = payment.amount,
                paymentDate = payment.creationDate,
                note = payment.note
            )

            val newPaidAmount = currentLoan.paid + payment.amount
            val isFullyPaid = newPaidAmount >= (currentLoan.quantity - 0.01)
            val newStatusId = if (isFullyPaid) 2L else 1L

            db.loanQueries.updatePaymentProgress(
                paidAmount = newPaidAmount,
                statusId = newStatusId,
                updateDate = DateMethods.getCurrentTimeMillis(),
                id = currentLoan.id.toLong()
            )
        }
    }

    override suspend fun getPendingLoans(): List<LoanWithCustomerName> = withContext(Dispatchers.IO) {
        db.loanQueries.selectPendingLoans().executeAsList().map {
            LoanWithCustomerName(
                loan = Loan(
                    id = it.id.toInt(),
                    customerId = it.customer_id.toInt(),
                    interestRate = it.interest_rate,
                    description = it.description,
                    paymentDate = it.payment_date,
                    paymentType = it.currencyName ?: "USD",
                    quantity = it.amount,
                    paid = it.paid,
                    statusId = it.status_id.toInt(),
                    creationDate = it.creation_date,
                    updateDate = it.update_date
                ),
                customerName = it.customerName,
                currency = it.currencyName ?: "USD"
            )
        }
    }
}