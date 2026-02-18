package com.yolbertdev.coffeeplatform.ui.main.screens.loan.add

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.yolbertdev.coffeeplatform.domain.model.Customer
import com.yolbertdev.coffeeplatform.ui.components.FormField
import com.yolbertdev.coffeeplatform.ui.components.PrimaryButton
import com.yolbertdev.coffeeplatform.ui.components.SecondaryTextFieldApp
import com.yolbertdev.coffeeplatform.util.DateMethods
import kotlinx.coroutines.delay

class AddLoanScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<AddLoanScreenModel>()
        val state by viewModel.uiState.collectAsState()

        val snackbarHostState = remember { SnackbarHostState() }

        var showPaymentDatePicker by remember { mutableStateOf(false) }
        var showCreationDatePicker by remember { mutableStateOf(false) }

        val paymentDatePickerState = rememberDatePickerState(initialSelectedDateMillis = state.paymentDate)
        val creationDatePickerState = rememberDatePickerState(initialSelectedDateMillis = state.creationDate)

        LaunchedEffect(state.showSuccessMessage) {
            if (state.showSuccessMessage) {
                snackbarHostState.showSnackbar("¡Préstamo creado correctamente!")
                delay(1000)
                viewModel.onNavigationHandled()
                navigator.pop()
            }
        }

        if (showPaymentDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showPaymentDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        paymentDatePickerState.selectedDateMillis?.let { viewModel.onPaymentDateChanged(it) }
                        showPaymentDatePicker = false
                    }) { Text("Aceptar") }
                },
                dismissButton = { TextButton(onClick = { showPaymentDatePicker = false }) { Text("Cancelar") } }
            ) { DatePicker(state = paymentDatePickerState) }
        }

        if (showCreationDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showCreationDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        creationDatePickerState.selectedDateMillis?.let { viewModel.onCreationDateChanged(it) }
                        showCreationDatePicker = false
                    }) { Text("Aceptar") }
                },
                dismissButton = { TextButton(onClick = { showCreationDatePicker = false }) { Text("Cancelar") } }
            ) { DatePicker(state = creationDatePickerState) }
        }

        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                TopAppBar(
                    title = { Text("Nuevo Préstamo", style = MaterialTheme.typography.titleMedium) },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(Icons.Rounded.ArrowBackIosNew, "Atrás")
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Spacer(Modifier.height(8.dp))

                // 1. Selector de Cliente
                CustomerDropdown(
                    customers = state.customers,
                    selectedCustomer = state.selectedCustomer,
                    onCustomerSelected = { viewModel.onCustomerSelected(it) },
                    error = state.customerError
                )

                // 2. Monto
                FormField(
                    label = "Monto a prestar",
                    value = state.amount,
                    onValueChange = { viewModel.onAmountChanged(it) },
                    icon = Icons.Default.AttachMoney,
                    error = state.amountError
                )

                // 3. Moneda (Chips con estilo)
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        "Tipo de Moneda",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)),
                        modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        val currencies = listOf("Dólares (USD)", "Quintales (QT)")
                        currencies.forEachIndexed { index, label ->
                            FilterChip(
                                selected = state.selectedCurrencyIndex == index,
                                onClick = { viewModel.onCurrencyChanged(index) },
                                label = { Text(label) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    }
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                // 4. Fechas
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    FormField(
                        label = "Fecha Emisión",
                        value = DateMethods.formatDate(state.creationDate),
                        onValueChange = {},
                        icon = Icons.Default.DateRange,
                        readOnly = true,
                        onClick = { showCreationDatePicker = true },
                        modifier = Modifier.weight(1f)
                    )
                    FormField(
                        label = "Vencimiento",
                        value = DateMethods.formatDate(state.paymentDate),
                        onValueChange = {},
                        icon = Icons.Default.Event,
                        readOnly = true,
                        onClick = { showPaymentDatePicker = true },
                        modifier = Modifier.weight(1f)
                    )
                }

                // 6. Interés y Descripción
                FormField(
                    label = "Tasa de Interés (%)",
                    value = state.interestRate,
                    onValueChange = { viewModel.onInterestChanged(it) },
                    icon = Icons.Default.Percent
                )

                FormField(
                    label = "Notas Adicionales",
                    value = state.description,
                    onValueChange = { viewModel.onDescriptionChanged(it) },
                    icon = Icons.Default.Description,
                    isLong = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (state.isLoading) {
                    CircularProgressIndicator()
                } else {
                    PrimaryButton(
                        text = "Crear Préstamo",
                        onClick = {
                            viewModel.saveLoan(
                                onSuccess = { navigator.pop() }
                            )
                        },
                        modifier = Modifier.fillMaxWidth().height(56.dp)
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerDropdown(
    customers: List<Customer>,
    selectedCustomer: Customer?,
    onCustomerSelected: (Customer) -> Unit,
    error: String? = null
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Cliente",
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)),
            modifier = Modifier.padding(start = 8.dp, bottom = 4.dp)
        )
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            SecondaryTextFieldApp(
                value = selectedCustomer?.name ?: "",
                onValueChange = {},
                readOnly = true,
                placeholder = { Text("Seleccionar Cliente", color = MaterialTheme.colorScheme.outline) },
                leadingIcon = { Icon(Icons.Default.Person, null, modifier = Modifier.size(20.dp), tint = MaterialTheme.colorScheme.primary) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                if (customers.isEmpty()) {
                    DropdownMenuItem(text = { Text("No hay clientes") }, onClick = { expanded = false })
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
        if (error != null) {
            Text(error, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(start = 12.dp, top = 2.dp))
        }
    }
}