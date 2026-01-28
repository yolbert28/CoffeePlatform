package com.yolbertdev.coffeeplatform.domain.ports

import com.yolbertdev.coffeeplatform.domain.model.ClientDebt

// Tipos de Reportes con sus encabezados definidos
enum class ReportType(val title: String, val headers: List<String>) {
    DEBTORS("Reporte de Deudores", listOf("Cliente", "Fecha", "Monto", "Unidad")),
    PAYMENTS("Reporte de Pagos", listOf("Cliente", "Fecha", "Monto", "Unidad")),
    LOANS("Reporte de Préstamos", listOf("Cliente", "Fecha", "Monto", "Unidad"))
}

// Modelo de fila genérico
data class ReportRow(
    val col1: String, // Nombre
    val col2: String, // Fecha
    val col3: String, // Monto
    val col4: String  // Unidad (USD / QT)
)

interface PdfGenerator {
    suspend fun generateReport(
        type: ReportType,
        data: List<ReportRow>,
        filterDescription: String
    ): String
}