package com.yolbertdev.coffeeplatform.pdf

import com.yolbertdev.coffeeplatform.domain.ports.PdfGenerator
import com.yolbertdev.coffeeplatform.domain.ports.ReportRow
import com.yolbertdev.coffeeplatform.domain.ports.ReportType

class JvmPdfGenerator : PdfGenerator {
    override suspend fun generateReport(
        type: ReportType,
        data: List<ReportRow>,
        filterDescription: String
    ): String {
        // TODO: Implementar la generación de PDF para escritorio (p. ej., con iText o PDFBox)
        println("Generando PDF para ${type.title} con ${data.size} filas...")
        // Devolver una ruta de archivo falsa por ahora
        return "/tmp/report.pdf"
    }
}
