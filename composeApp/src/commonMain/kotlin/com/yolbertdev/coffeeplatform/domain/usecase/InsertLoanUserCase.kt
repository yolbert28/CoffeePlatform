package com.yolbertdev.coffeeplatform.domain.usecase

import com.yolbertdev.coffeeplatform.domain.model.Loan
import com.yolbertdev.coffeeplatform.domain.repository.LoanRepository

class InsertLoanUseCase(
    private val repository: LoanRepository
) {
    suspend operator fun invoke(loan: Loan) {
        repository.insert(loan)
    }
}