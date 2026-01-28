package com.yolbertdev.coffeeplatform.data.database.dao

import com.yolbertdev.coffeeplatform.db.CoffeeDatabase

class LoanDao(
    private val database: CoffeeDatabase
) {
    private val queries = database.loanQueries

    fun getLoansForReport() = queries.selectLoansForReport().executeAsList()

    // Se puede agregar otros métodos como insert, etc.
}