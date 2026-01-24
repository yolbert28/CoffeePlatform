package com.yolbertdev.coffeeplatform.pdf

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import com.yolbertdev.coffeeplatform.domain.model.ClientDebt
import com.yolbertdev.coffeeplatform.domain.ports.PdfGenerator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class AndroidPdfGenerator(
    private val context: Context
) : PdfGenerator {

    override suspend fun generateDebtReport(clients: List<ClientDebt>): String = withContext(Dispatchers.IO) {
        val document = PdfDocument()

        // 1. Configuración de la página (A4 estándar aprox: 595 x 842 puntos)
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = document.startPage(pageInfo)
        val canvas = page.canvas

        // 2. Estilos
        val titlePaint = Paint().apply {
            color = Color.BLACK
            textSize = 24f
            isFakeBoldText = true
        }
        val textPaint = Paint().apply {
            color = Color.DKGRAY
            textSize = 14f
        }
        val linePaint = Paint().apply {
            color = Color.GRAY
            strokeWidth = 1f
        }

        // 3. Dibujar Encabezado
        var yPosition = 60f
        canvas.drawText("Reporte de Deudas - CoffeePlatform", 40f, yPosition, titlePaint)

        yPosition += 40f
        canvas.drawLine(40f, yPosition, 550f, yPosition, linePaint)

        // 4. Dibujar Lista de Clientes (Datos estáticos por ahora)
        yPosition += 30f

        // Cabecera de la tabla
        canvas.drawText("Cliente", 40f, yPosition, textPaint)
        canvas.drawText("Fecha", 300f, yPosition, textPaint)
        canvas.drawText("Monto", 450f, yPosition, textPaint)

        yPosition += 10f
        canvas.drawLine(40f, yPosition, 550f, yPosition, linePaint)
        yPosition += 30f

        // Filas
        for (client in clients) {
            canvas.drawText(client.name, 40f, yPosition, textPaint)
            canvas.drawText(client.date, 300f, yPosition, textPaint)
            canvas.drawText("$${client.amount}", 450f, yPosition, textPaint)

            yPosition += 25f

            // Lógica simple de paginación (si se acaba la hoja)
            if (yPosition > 800) {
                // En un caso real, aquí cerrarías la página actual y abrirías una nueva
                break
            }
        }

        // 5. Finalizar página
        document.finishPage(page)

        // 6. Guardar archivo
        val directory = context.getExternalFilesDir(null) ?: context.filesDir
        val file = File(directory, "Reporte_Deudas.pdf")

        try {
            FileOutputStream(file).use { outputStream ->
                document.writeTo(outputStream)
            }
        } catch (e: IOException) {
            e.printStackTrace()
            // Manejo de error limpio
            document.close()
            throw e
        }

        document.close()
        return@withContext file.absolutePath
    }
}
