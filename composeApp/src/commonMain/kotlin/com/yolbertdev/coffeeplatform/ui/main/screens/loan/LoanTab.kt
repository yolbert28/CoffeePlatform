package com.yolbertdev.coffeeplatform.ui.main.screens.loan
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import coffeeplatform.composeapp.generated.resources.Res
import coffeeplatform.composeapp.generated.resources.dollar
import com.yolbertdev.coffeeplatform.ui.components.FilterSelector
import com.yolbertdev.coffeeplatform.ui.components.LoanItem
import com.yolbertdev.coffeeplatform.ui.components.SearchBarApp
import com.yolbertdev.coffeeplatform.ui.main.screens.loan.add.AddLoanScreen
import com.yolbertdev.coffeeplatform.ui.main.screens.loan.detail.LoanDetailScreen
import org.jetbrains.compose.resources.painterResource

object LoanTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val icon = painterResource(Res.drawable.dollar)
            return remember {
                TabOptions(
                    index = 3u,
                    title = "Préstamos",
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<LoanScreenModel>()
        val state by viewModel.uiState.collectAsState()

        // Recargar datos cada vez que se muestra la pantalla
        LaunchedEffect(Unit) {
            viewModel.loadLoans()
        }

        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navigator.parent?.push(AddLoanScreen()) },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Nuevo Préstamo")
                }
            }
        ) { paddingValues ->
            Column(
                Modifier
                    .padding(paddingValues)
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(Modifier.height(12.dp))

                SearchBarApp(
                    value = state.searchQuery,
                    onValueChange = { viewModel.onSearchQueryChanged(it) },
                    placeholder = "Buscar por cliente o descripción..."
                )

                Spacer(Modifier.height(4.dp))
                FilterSelector() // Tu componente de filtros existente

                if (state.isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 80.dp) // Espacio para el FAB
                    ) {
                        if (state.filteredLoans.isEmpty()) {
                            item {
                                Text(
                                    text = "No hay préstamos registrados",
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(top = 24.dp)
                                )
                            }
                        } else {
                            items(state.filteredLoans) { (loan, customer) ->
                                LoanItem(loan = loan, onClick = {
                                    // Ahora tenemos tanto el Préstamo como el Cliente real para pasar al detalle
                                    navigator.parent?.push(LoanDetailScreen(loan, customer))
                                })
                            }
                        }
                    }
                }
            }
        }
    }
}