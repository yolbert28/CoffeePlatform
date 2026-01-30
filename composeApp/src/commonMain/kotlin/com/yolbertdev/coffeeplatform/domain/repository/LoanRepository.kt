package com.yolbertdev.coffeeplatform.domain.repository
import com.yolbertdev.coffeeplatform.domain.model.Customer
import com.yolbertdev.coffeeplatform.domain.model.Loan
import com.yolbertdev.coffeeplatform.domain.ports.ReportRow

interface LoanRepository {
    suspend fun getLoansForReport(): List<ReportRow>
    suspend fun insert(loan: Loan)
    suspend fun getLoansByCustomerId(customerId: Long): List<Loan>
    suspend fun getAllLoansWithCustomer(): List<Pair<Loan, Customer>>
}