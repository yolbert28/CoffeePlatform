package com.yolbertdev.coffeeplatform.ui.main.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import coffeeplatform.composeapp.generated.resources.Res
import coffeeplatform.composeapp.generated.resources.download
import com.yolbertdev.coffeeplatform.presentation.ReportUiState
import com.yolbertdev.coffeeplatform.presentation.ReportViewModel
import org.jetbrains.compose.resources.painterResource

// 1. Creamos un "Túnel" (CompositionLocal) para pasar la acción de abrir PDF de forma invisible
val LocalPdfOpener = staticCompositionLocalOf<(String) -> Unit> {
    error("El PdfOpener no ha sido provisto en la raíz de la app")
}

object ReportTab : Tab {

    override val options: TabOptions
        @Composable
        get(){
            val icon = painterResource(Res.drawable.download)
            return remember {
                TabOptions(
                    index = 4u,
                    title = "Reportes",
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        // 2. Inyectamos el ViewModel automáticamente con Koin
        val viewModel = getScreenModel<ReportViewModel>()
        val state by viewModel.uiState.collectAsState()

        // 3. Obtenemos la función para abrir PDF del entorno
        val onOpenPdf = LocalPdfOpener.current

        // 4. Renderizamos la UI
        ReportScreenContent(
            state = state,
            onGenerateClick = { viewModel.generateReport() },
            onOpenClick = { path -> onOpenPdf(path) }
        )
    }
}

@Composable
fun ReportScreenContent(
    state: ReportUiState,
    onGenerateClick: () -> Unit,
    onOpenClick: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Description,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Generación de Reportes",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(32.dp))

        if (state.isLoading) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
            Text("Generando documento...")
        } else {
            // Botón Generar
            Button(
                onClick = onGenerateClick,
                // Opcional: Deshabilitar si ya se generó para obligar a limpiar o regenerar
                enabled = true
            ) {
                Text("Generar Reporte de Deudas")
            }

            // Botón Abrir (Solo aparece si hay path)
            state.pdfPath?.let { path ->
                Spacer(modifier = Modifier.height(24.dp))

                OutlinedButton(
                    onClick = { onOpenClick(path) }
                ) {
                    Text("Abrir PDF Generado")
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Guardado en dispositivo",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            // Mensaje de Error
            state.error?.let { error ->
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}