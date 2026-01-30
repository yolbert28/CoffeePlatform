package com.yolbertdev.coffeeplatform.ui.main.screens.customer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import coffeeplatform.composeapp.generated.resources.Res
import coffeeplatform.composeapp.generated.resources.people
import com.yolbertdev.coffeeplatform.ui.components.CustomerListItem
import com.yolbertdev.coffeeplatform.ui.components.FilterSelector
import com.yolbertdev.coffeeplatform.ui.components.SearchBarApp
import com.yolbertdev.coffeeplatform.ui.main.screens.customer.add.AddCustomerScreen
import com.yolbertdev.coffeeplatform.ui.main.screens.customer.detail.CustomerDetailScreen
import org.jetbrains.compose.resources.painterResource

object CustomerTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val icon = painterResource(Res.drawable.people)
            return remember {
                TabOptions(
                    index = 1u,
                    title = "Clientes",
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow
        val screenModel = getScreenModel<CustomerScreenModel>()
        val uiState by screenModel.uiState.collectAsState()

        var searchQuery by remember { mutableStateOf("") }

        // 1. CARGA DE DATOS: Solo cargamos, ya no insertamos nada falso.
        LaunchedEffect(Unit) {
            screenModel.getAllCustomers()
        }

        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navigator.parent?.push(AddCustomerScreen()) },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar Cliente")
                }
            }
        ) { paddingValues ->
            Column(
                Modifier
                    .padding(paddingValues)
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(Modifier.height(12.dp))

                // Barra de búsqueda
                SearchBarApp(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = "Buscar por cliente o descripción..."
                )

                Spacer(Modifier.height(4.dp))
                FilterSelector()

                LazyColumn(
                    modifier = Modifier.padding(bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 2. LISTA REAL: Muestra los clientes que vienen del State (BD)
                    items(uiState.customers) { customer ->
                        CustomerListItem(customer = customer) {
                            navigator.parent?.push(CustomerDetailScreen(customer))
                        }
                    }
                }
            }
        }
    }
}