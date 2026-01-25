package com.yolbertdev.coffeeplatform.ui.main.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.yolbertdev.coffeeplatform.domain.ports.ReportType
import com.yolbertdev.coffeeplatform.presentation.ReportUiState
import com.yolbertdev.coffeeplatform.presentation.ReportViewModel
import org.jetbrains.compose.resources.painterResource
import coffeeplatform.composeapp.generated.resources.Res
import coffeeplatform.composeapp.generated.resources.download

// 1. Creamos un "Túnel" (CompositionLocal) para pasar la acción de abrir PDF de forma invisible
val LocalPdfOpener = staticCompositionLocalOf<(String) -> Unit> {
    error("PdfOpener no provisto")
}

object ReportTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val icon = painterResource(Res.drawable.download)
            return remember {
                TabOptions(index = 4u, title = "Reportes", icon = icon)
            }
        }

    @Composable
    override fun Content() {
        val viewModel = getScreenModel<ReportViewModel>()
        val state by viewModel.uiState.collectAsState()
        val onOpenPdf = LocalPdfOpener.current

        ReportScreenContent(
            state = state,
            months = viewModel.months,
            currencies = viewModel.currencies,
            onTypeSelected = { viewModel.selectReportType(it) },
            onMonthSelected = { viewModel.selectMonth(it) },
            onCurrencySelected = { viewModel.selectCurrency(it) },
            onGenerateClick = { viewModel.generateReport() },
            onOpenClick = { path -> onOpenPdf(path) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreenContent(
    state: ReportUiState,
    months: List<String>,
    currencies: List<String>,
    onTypeSelected: (ReportType) -> Unit,
    onMonthSelected: (String) -> Unit,
    onCurrencySelected: (String) -> Unit,
    onGenerateClick: () -> Unit,
    onOpenClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()), // Scroll por si la pantalla es chica
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // --- Cabecera ---
        Icon(
            imageVector = Icons.Default.Description,
            contentDescription = null,
            modifier = Modifier.size(48.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Centro de Reportes",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(24.dp))

        // --- Sección 1: Tipo de Reporte ---
        Text("Selecciona el tipo de reporte:", style = MaterialTheme.typography.labelLarge)
        Spacer(modifier = Modifier.height(8.dp))

        // Usamos LazyRow para los tipos si son muchos, o un Column simple
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ReportType.entries.forEach { type ->
                FilterChip(
                    selected = state.selectedType == type,
                    onClick = { onTypeSelected(type) },
                    label = { Text(type.title.replace("Reporte de ", "")) }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(24.dp))

        // --- Sección 2: Filtros ---
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.FilterList, contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Filtros", style = MaterialTheme.typography.titleMedium)
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Filtro Mes
        Text("Mes:", style = MaterialTheme.typography.labelMedium)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(months) { month ->
                FilterChip(
                    selected = state.selectedMonth == month,
                    onClick = { onMonthSelected(month) },
                    label = { Text(month) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Filtro Moneda (USD / QT)
        Text("Moneda / Unidad:", style = MaterialTheme.typography.labelMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            currencies.forEach { currency ->
                FilterChip(
                    selected = state.selectedCurrency == currency,
                    onClick = { onCurrencySelected(currency) },
                    label = { Text(currency) }
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- Sección 3: Acciones ---
        if (state.isLoading) {
            CircularProgressIndicator()
            Text("Generando PDF...", modifier = Modifier.padding(top = 8.dp))
        } else {
            Button(
                onClick = onGenerateClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Generar Reporte")
            }

            state.pdfPath?.let { path ->
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedButton(
                    onClick = { onOpenClick(path) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Abrir PDF")
                }
                Text(
                    text = "Archivo listo ✅",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            state.error?.let { error ->
                Spacer(modifier = Modifier.height(16.dp))
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)) {
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}