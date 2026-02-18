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
import com.yolbertdev.coffeeplatform.ui.main.screens.payment.add.AddPaymentScreen // Importar pantalla de agregar
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

        // Inyectamos el ViewModel de la lista de pagos
        val viewModel = getScreenModel<PaymentScreenModel>()
        val state by viewModel.state.collectAsState()

        var searchQuery by remember { mutableStateOf("") }

        // Recargar la lista cada vez que se entra a esta pantalla (útil al volver de agregar un pago)
        LifecycleEffect(
            onStarted = { viewModel.loadPayments() }
        )

        Scaffold(
            // --- AQUÍ AGREGAMOS EL BOTÓN FLOTANTE ---
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    text = { Text("Nuevo Pago") },
                    icon = { Icon(Icons.Default.Add, contentDescription = null) },
                    onClick = {
                        // Navegamos a la pantalla de crear pago
                        // Usamos navigator.parent porque estamos dentro de un Tab
                        navigator.parent?.push(AddPaymentScreen())
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
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

                FilterSelector() // (Visual por ahora)

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
                        // Filtramos localmente por búsqueda
                        val filteredList = state.payments

                        items(filteredList) { payment ->
                            MainPaymentItem(payment){
                            }
                        }

                        // Espacio extra al final para que el botón flotante no tape el último item
                        item { Spacer(Modifier.height(80.dp)) }
                    }
                }
            }
        }
    }
}