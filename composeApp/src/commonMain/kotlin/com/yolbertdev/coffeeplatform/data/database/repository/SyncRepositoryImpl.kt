package com.yolbertdev.coffeeplatform.data.database.repository


import com.yolbertdev.coffeeplatform.data.database.dao.SyncDao
import com.yolbertdev.coffeeplatform.domain.model.BackupData
import com.yolbertdev.coffeeplatform.domain.model.CustomerBackup
import com.yolbertdev.coffeeplatform.domain.model.LoanBackup
import com.yolbertdev.coffeeplatform.domain.model.PaymentBackup
import com.yolbertdev.coffeeplatform.domain.repository.SyncRepository
import com.yolbertdev.coffeeplatform.util.DateMethods

class SyncRepositoryImpl(
    private val dao: SyncDao
) : SyncRepository {

    override suspend fun exportAllData(): BackupData {
        val customers = dao.selectAllCustomers().map { c ->
            CustomerBackup(
                id = c.id,
                idCard = c.id_card,
                name = c.name,
                nickname = c.nickname,
                description = c.description,
                creditLevel = c.credit_level,
                location = c.location,
                photo = c.photo,
                creationDate = c.creation_date,
                updateDate = c.update_date,
                statusId = c.status_id
            )
        }

        val loans = dao.selectAllLoans().map { l ->
            LoanBackup(
                id = l.id.toInt(),
                customerId = l.customer_id.toInt(),
                interestRate = l.interest_rate,
                paymentTypeId = l.payment_type_id,
                amount = l.amount,
                paymentDate = l.payment_date,
                paid = l.paid,
                description = l.description,
                creationDate = l.creation_date,
                updateDate = l.update_date,
                statusId = l.status_id.toInt()
            )
        }

        val payments = dao.selectAllPayments().map { p ->
            PaymentBackup(
                id = p.id,
                loanId = p.loan_id,
                amount = p.amount,
                paymentDate = p.payment_date,
                note = p.note,
                creationDate = p.creation_date,
                updateDate = p.update_date
            )
        }

        return BackupData(
            exportDate = DateMethods.getCurrentTimeMillis(),
            customers = customers,
            loans = loans,
            payments = payments
        )
    }

    override suspend fun importAllData(backupData: BackupData) {
        // 1. Insertar clientes que no existen
        for (customer in backupData.customers) {
            if (!dao.customerExists(customer.id)) {
                dao.insertCustomerRaw(
                    idCard = customer.idCard,
                    name = customer.name,
                    nickname = customer.nickname,
                    description = customer.description,
                    creditLevel = customer.creditLevel,
                    location = customer.location,
                    photo = customer.photo?:"",
                    statusId = customer.statusId
                )
            }
        }

        // 2. Insertar préstamos que no existen
        for (loan in backupData.loans) {
            if (!dao.loanExistsById(loan.id.toLong())) {
                dao.insertLoanRaw(
                    customerId = loan.customerId.toLong(),
                    interestRate = loan.interestRate,
                    paymentTypeId = loan.paymentTypeId,
                    amount = loan.amount,
                    paymentDate = loan.paymentDate,
                    paid = loan.paid,
                    description = loan.description,
                    statusId = loan.statusId.toLong(),
                    creationDate = loan.creationDate,
                    updateDate = loan.updateDate
                )
            }
        }

        // 3. Insertar pagos
        for (payment in backupData.payments) {
            dao.insertPaymentRaw(
                loanId = payment.loanId,
                amount = payment.amount,
                paymentDate = payment.paymentDate,
                note = payment.note
            )
        }
    }
}