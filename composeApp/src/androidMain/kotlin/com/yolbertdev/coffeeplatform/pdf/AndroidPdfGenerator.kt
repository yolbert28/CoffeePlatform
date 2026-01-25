package com.yolbertdev.coffeeplatform.pdf

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import com.yolbertdev.coffeeplatform.domain.model.ClientDebt
import com.yolbertdev.coffeeplatform.domain.ports.PdfGenerator
import com.yolbertdev.coffeeplatform.domain.ports.ReportRow
import com.yolbertdev.coffeeplatform.domain.ports.ReportType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class AndroidPdfGenerator(
    private val context: Context
) : PdfGenerator {

    override suspend fun generateReport(
        type: ReportType,
        data: List<ReportRow>,
        filterDescription: String
    ): String = withContext(Dispatchers.IO) {
        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4
        val page = document.startPage(pageInfo)
        val canvas = page.canvas

        // --- Estilos ---
        val titlePaint = Paint().apply { color = Color.BLACK; textSize = 24f; isFakeBoldText = true }
        val subtitlePaint = Paint().apply { color = Color.DKGRAY; textSize = 14f }
        val headerPaint = Paint().apply { color = Color.BLACK; textSize = 12f; isFakeBoldText = true }
        val textPaint = Paint().apply { color = Color.DKGRAY; textSize = 12f }
        val linePaint = Paint().apply { color = Color.LTGRAY; strokeWidth = 1f }

        var yPos = 60f

        // 1. Título y Subtítulo
        canvas.drawText(type.title, 40f, yPos, titlePaint)
        yPos += 25f
        canvas.drawText(filterDescription, 40f, yPos, subtitlePaint)
        yPos += 20f
        canvas.drawLine(40f, yPos, 550f, yPos, linePaint)
        yPos += 30f

        // 2. Encabezados (Coordenadas ajustadas para 4 columnas)
        // Col1: 40, Col2: 240, Col3: 360, Col4: 480
        val headers = type.headers
        canvas.drawText(headers[0], 40f, yPos, headerPaint)
        canvas.drawText(headers[1], 240f, yPos, headerPaint)
        canvas.drawText(headers[2], 360f, yPos, headerPaint)
        canvas.drawText(headers[3], 480f, yPos, headerPaint)

        yPos += 10f
        canvas.drawLine(40f, yPos, 550f, yPos, linePaint)
        yPos += 20f

        // 3. Datos
        for (item in data) {
            canvas.drawText(item.col1, 40f, yPos, textPaint)
            canvas.drawText(item.col2, 240f, yPos, textPaint)
            canvas.drawText(item.col3, 360f, yPos, textPaint)
            canvas.drawText(item.col4, 480f, yPos, textPaint)

            yPos += 20f

            // Paginación simple
            if (yPos > 800) break
        }

        document.finishPage(page)

        // Guardar archivo
        val fileName = "Report_${type.name}_${System.currentTimeMillis()}.pdf"
        val file = File(context.getExternalFilesDir(null) ?: context.filesDir, fileName)

        try {
            FileOutputStream(file).use { document.writeTo(it) }
        } catch (e: IOException) {
            document.close()
            throw e
        }

        document.close()
        return@withContext file.absolutePath
    }
}