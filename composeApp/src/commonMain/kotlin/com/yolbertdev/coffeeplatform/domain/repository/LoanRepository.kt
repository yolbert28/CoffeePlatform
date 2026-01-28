package com.yolbertdev.coffeeplatform.domain.repository

import com.yolbertdev.coffeeplatform.domain.ports.ReportRow

interface LoanRepository {
    // Retornamos directamente ReportRow para facilitar el reporte,
    // o podrías retornar un modelo de dominio "LoanWithClient" y mapearlo después.
    suspend fun getLoansForReport(): List<ReportRow>
}