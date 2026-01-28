package com.yolbertdev.coffeeplatform.data.database.repository

import com.yolbertdev.coffeeplatform.db.CoffeeDatabase
import com.yolbertdev.coffeeplatform.domain.repository.LoanRepository
import com.yolbertdev.coffeeplatform.domain.ports.ReportRow
import com.yolbertdev.coffeeplatform.util.DateMethods



class LoanRepositoryImpl(
    private val db: CoffeeDatabase
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
}