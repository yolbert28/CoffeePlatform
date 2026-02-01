package com.yolbertdev.coffeeplatform.ui.main.screens.payment

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
import cafe.adriel.voyager.core.lifecycle.LifecycleEffect
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import coffeeplatform.composeapp.generated.resources.Res
import coffeeplatform.composeapp.generated.resources.payment
import com.yolbertdev.coffeeplatform.ui.components.FilterSelector
import com.yolbertdev.coffeeplatform.ui.components.MainPaymentItem
import com.yolbertdev.coffeeplatform.ui.components.SearchBarApp
import com.yolbertdev.coffeeplatform.ui.main.screens.payment.add.AddPaymentScreen
import org.jetbrains.compose.resources.painterResource

object PaymentTab : Tab {

    override val options: TabOptions
        @Composable
        get() {
            val icon = painterResource(Res.drawable.payment)
            return remember {
                TabOptions(
                    index = 2u,
                    title = "Pagos",
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<PaymentScreenModel>()
        val state by viewModel.state.collectAsState()

        var searchQuery by remember { mutableStateOf("") }

        // --- MAGIA: Recargar datos cada vez que la pantalla se muestra ---
        // Esto asegura que si agregas un pago y vuelves, la lista se actualice.
        LifecycleEffect(
            onStarted = { viewModel.loadPayments() }
        )

        Scaffold(
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    text = { Text("Nuevo Pago") },
                    icon = { Icon(Icons.Default.Add, contentDescription = null) },
                    onClick = {
                        navigator.parent?.push(AddPaymentScreen())
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(Modifier.height(12.dp))

                SearchBarApp(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = "Buscar pago..."
                )

                Spacer(Modifier.height(4.dp))

                FilterSelector() // (Nota: Aún es visual, habría que conectarlo al VM si quieres filtrar)

                if (state.isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else if (state.payments.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No hay pagos registrados", color = MaterialTheme.colorScheme.outline)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.padding(bottom = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Filtramos localmente por búsqueda si el usuario escribe algo
                        val filteredList = state.payments.filter {
                            it.col1.contains(searchQuery, ignoreCase = true)
                        }

                        items(filteredList) { row ->
                            // row.col1 = Nombre Cliente
                            // row.col2 = Fecha
                            // row.col3 = Monto
                            // row.col4 = Moneda

                            MainPaymentItem(
                                customerNickname = row.col1.uppercase(), // Iniciales como nickname
                                customerName = row.col1,
                                customerPhoto = "", // Sin foto por ahora
                                amount = "${row.col3} ${row.col4}",
                                date = row.col2,
                                onClick = {
                                    // Aquí podrías navegar al detalle si tuvieras el ID del pago
                                    // Como ReportRow no tiene ID, por ahora lo dejamos sin acción o
                                    // agregas un Toast informativo.
                                }
                            )
                        }

                        item { Spacer(Modifier.height(60.dp)) } // Espacio para el FAB
                    }
                }
            }
        }
    }
}