package com.yolbertdev.coffeeplatform.ui.main.screens.loan.add

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.yolbertdev.coffeeplatform.ui.components.PrimaryButton
import com.yolbertdev.coffeeplatform.ui.components.TextFieldApp

class AddLoanScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<AddLoanScreenModel>()
        val state by viewModel.uiState.collectAsState()

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Nuevo Préstamo") },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // 1. Selector de Cliente (Dropdown)
                CustomerDropdown(
                    customers = state.customers,
                    selectedCustomer = state.selectedCustomer,
                    onCustomerSelected = { viewModel.onCustomerSelected(it) }
                )

                // 2. Monto
                TextFieldApp(
                    value = state.amount,
                    onValueChange = { viewModel.onAmountChanged(it) },
                    label = "Monto a prestar",
                    imageVector = Icons.Default.AttachMoney
                )

                // 3. Moneda (Segmented Button simulado o Chips)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val currencies = listOf("Dólares (USD)", "Quintales (QT)")
                    currencies.forEachIndexed { index, label ->
                        FilterChip(
                            selected = state.selectedCurrencyIndex == index,
                            onClick = { viewModel.onCurrencyChanged(index) },
                            label = { Text(label) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // 4. Interés
                TextFieldApp(
                    value = state.interestRate,
                    onValueChange = { viewModel.onInterestChanged(it) },
                    label = "Tasa de Interés (%)",
                    imageVector = Icons.Default.Percent
                )

                // 5. Descripción
                TextFieldApp(
                    value = state.description,
                    onValueChange = { viewModel.onDescriptionChanged(it) },
                    label = "Descripción / Notas",
                    imageVector = Icons.Default.Description
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 6. Botón Guardar
                if (state.isLoading) {
                    CircularProgressIndicator()
                } else {
                    PrimaryButton(
                        text = "Crear Préstamo",
                        onClick = {
                            viewModel.saveLoan(onSuccess = {
                                navigator.pop() // Regresar a la lista
                            })
                        },
                        enabled = state.selectedCustomer != null && state.amount.isNotEmpty()
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerDropdown(
    customers: List<com.yolbertdev.coffeeplatform.domain.model.Customer>,
    selectedCustomer: com.yolbertdev.coffeeplatform.domain.model.Customer?,
    onCustomerSelected: (com.yolbertdev.coffeeplatform.domain.model.Customer) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedCustomer?.name ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text("Seleccionar Cliente") },
            leadingIcon = { Icon(Icons.Default.Person, null) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            modifier = Modifier.fillMaxWidth().menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            if (customers.isEmpty()) {
                DropdownMenuItem(
                    text = { Text("No hay clientes registrados") },
                    onClick = { expanded = false }
                )
            } else {
                customers.forEach { customer ->
                    DropdownMenuItem(
                        text = { Text(customer.name) },
                        onClick = {
                            onCustomerSelected(customer)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}