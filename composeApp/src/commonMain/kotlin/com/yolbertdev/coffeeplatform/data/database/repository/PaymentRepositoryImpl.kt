package com.yolbertdev.coffeeplatform.data.database.repository

import com.yolbertdev.coffeeplatform.data.database.dao.PaymentDao
import com.yolbertdev.coffeeplatform.db.CoffeeDatabase
import com.yolbertdev.coffeeplatform.domain.model.Customer
import com.yolbertdev.coffeeplatform.domain.model.Loan
import com.yolbertdev.coffeeplatform.domain.model.Payment
import com.yolbertdev.coffeeplatform.domain.ports.ReportRow
import com.yolbertdev.coffeeplatform.domain.repository.LoanWithCustomerName
import com.yolbertdev.coffeeplatform.domain.repository.PaymentRepository
import com.yolbertdev.coffeeplatform.util.DateMethods
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class PaymentRepositoryImpl(
    private val db: CoffeeDatabase,
    private val dao: PaymentDao
) : PaymentRepository {

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

    override suspend fun getPaymentsForReport(): List<ReportRow> {
        return dao.getPaymentsForReport().map {
            ReportRow(
                col1 = it.clientName,
                col2 = DateMethods.formatDate(it.payment_date),
                col3 = it.amount.toString(),
                col4 = it.currencyName ?: "N/A"
            )
        }
    }

    override fun getPaymentsWithDetails(): Flow<List<Payment>> {
        return dao.getPaymentsWithDetails().map { list ->
            list.map {
                Payment(
                    id = it.payment_id,
                    loanId = it.loan_id.toInt(),
                    amount = it.amount,
                    paymentType = it.currency_name ?: "USD",
                    customerId = it.customer_id.toInt(),
                    creationDate = it.payment_date,
                    updateDate = it.payment_update_date,
                    note = it.note,
                    customer = Customer(
                        id = it.customer_id,
                        name = it.customer_name,
                        nickname = it.customer_nickname,
                        location = it.customer_location,
                        idCard = it.customer_id_card ?: "",
                        description = it.customer_description,
                        photo = it.customer_photo,
                        creditLevel = it.customer_credit_level,
                        statusId = it.customer_status_id,
                        creationDate = it.customer_creation_date,
                        updateDate = it.customer_update_date
                    )
                )
            }
        }
    }

    override suspend fun getPaymentsByCustomerId(customerId: Long): List<Payment> {
        return dao.getPaymentsByCustomer(customerId).map {
            Payment(
                id = it.id,
                amount = it.amount,
                loanId = it.loan_id.toInt(),
                creationDate = it.payment_date,
                note = it.note ?: "",
                paymentType = if (it.payment_type_id == 1L) "USD" else "QT",
                customerId = it.customer_id.toInt(),
                updateDate = it.update_date
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