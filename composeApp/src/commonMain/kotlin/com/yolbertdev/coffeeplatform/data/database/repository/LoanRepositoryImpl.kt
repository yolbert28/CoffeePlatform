package com.yolbertdev.coffeeplatform.data.database.repository

import com.yolbertdev.coffeeplatform.data.database.dao.LoanDao
import com.yolbertdev.coffeeplatform.db.CoffeeDatabase
import com.yolbertdev.coffeeplatform.domain.model.Customer
import com.yolbertdev.coffeeplatform.domain.model.Loan
import com.yolbertdev.coffeeplatform.domain.repository.LoanRepository
import com.yolbertdev.coffeeplatform.domain.ports.ReportRow
import com.yolbertdev.coffeeplatform.util.DateMethods

class LoanRepositoryImpl(
    private val db: CoffeeDatabase,
    private val dao: LoanDao
) : LoanRepository {

    override suspend fun getLoansForReport(): List<ReportRow> {
        return db.loanQueries.selectLoansForReport().executeAsList().map {
            ReportRow(
                col1 = it.clientName,
                col2 = DateMethods.formatDate(it.creation_date),
                col3 = it.amount.toString(),
                col4 = it.currencyName ?: "N/A"
            )
        }
    }

    override suspend fun insert(loan: Loan) {
        // Obtenemos el ID del tipo de pago desde la base de datos idealmente,
        // pero por ahora mantenemos tu lógica hardcoded para coincidir con tus datos iniciales.
        val paymentTypeId = if (loan.paymentType == "USD") 1L else 2L

        dao.insert(
            customer_id = loan.customerId.toLong(),
            interest_rate = loan.interestRate,
            payment_type_id = paymentTypeId,
            amount = loan.quantity,
            payment_date = loan.paymentDate,
            paid = loan.paid,
            description = loan.description,
            status_id = loan.statusId.toLong(),
            creation_date = loan.creationDate, // AQUI ESTÁ LA SOLUCIÓN (Milisegundos)
            update_date = loan.updateDate      // AQUI TAMBIÉN
        )
    }

    override suspend fun getLoansByCustomerId(customerId: Long): List<Loan> {
        return dao.getLoansByCustomer(customerId).map {
            Loan(
                id = it.id.toInt(),
                customerId = it.customer_id.toInt(),
                interestRate = it.interest_rate,
                description = it.description,
                paymentDate = it.payment_date,
                paymentType = if (it.payment_type_id == 1L) "USD" else "QT",
                quantity = it.amount,
                paid = it.paid,
                statusId = it.status_id.toInt(),
                creationDate = it.creation_date,
                updateDate = it.update_date
            )
        }
    }

    override suspend fun getAllLoansWithCustomer(): List<Pair<Loan, Customer>> {
        return dao.getAllLoansWithCustomer()
    }
}