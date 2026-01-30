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
                col2 = DateMethods.formatDate(it.creation_date), // Asegúrate de tener este método o usa toString() temporalmente
                col3 = it.amount.toString(),
                col4 = it.currencyName ?: "N/A"
            )
        }
    }
    override suspend fun insert(loan: Loan) {
        val paymentTypeId = if (loan.paymentType == "USD") 1L else 2L

        dao.insert(
            customer_id = loan.customerId.toLong(),
            interest_rate = loan.interestRate, // Pasa directo (Double)
            payment_type_id = paymentTypeId,
            amount = loan.quantity,
            payment_date = loan.paymentDate,
            paid = loan.paid,                  // Pasa directo (Double)
            description = loan.description,
            status_id = loan.statusId.toLong()
        )
    }
    override suspend fun getLoansByCustomerId(customerId: Long): List<Loan> {
        return dao.getLoansByCustomer(customerId).map {
            Loan(
                id = it.id.toInt(),
                customerId = it.customer_id.toInt(),
                interestRate = it.interest_rate, // Double
                description = it.description,
                paymentDate = it.payment_date, // Long
                paymentType = if (it.payment_type_id == 1L) "USD" else "QT", // Mapeo inverso simple
                quantity = it.amount,
                paid = it.paid,
                statusId = it.status_id.toInt(),
                creationDate = it.creation_date,
                updateDate = 0L // Si no lo guardas en BD, pon 0 o agrégalo a la tabla
            )
        }
    }
    override suspend fun getAllLoansWithCustomer(): List<Pair<Loan, Customer>> {
        // Como el DAO ya retorna objetos de Dominio (ver LoanDao abajo),
        // simplemente retornamos el resultado.
        return dao.getAllLoansWithCustomer()
    }
}