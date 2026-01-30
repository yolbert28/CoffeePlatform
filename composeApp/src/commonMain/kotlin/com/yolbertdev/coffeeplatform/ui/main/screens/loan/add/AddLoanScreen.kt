package com.yolbertdev.coffeeplatform.ui.main.screens.loan.add

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.yolbertdev.coffeeplatform.domain.model.Customer
import com.yolbertdev.coffeeplatform.ui.components.PrimaryButton
import com.yolbertdev.coffeeplatform.ui.components.TextFieldApp
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

        // --- ESTADOS PARA LOS DOS CALENDARIOS ---
        var showPaymentDatePicker by remember { mutableStateOf(false) }
        var showCreationDatePicker by remember { mutableStateOf(false) }

        // State del picker de Pago
        val paymentDatePickerState = rememberDatePickerState(
            initialSelectedDateMillis = state.paymentDate
        )
        // State del picker de Creación
        val creationDatePickerState = rememberDatePickerState(
            initialSelectedDateMillis = state.creationDate
        )

        LaunchedEffect(state.showSuccessMessage) {
            if (state.showSuccessMessage) {
                snackbarHostState.showSnackbar("¡Préstamo creado correctamente!")
                delay(1000)
                viewModel.onNavigationHandled()
                navigator.pop()
            }
        }

        // --- DIÁLOGO FECHA DE PAGO ---
        if (showPaymentDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showPaymentDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        paymentDatePickerState.selectedDateMillis?.let { date ->
                            viewModel.onPaymentDateChanged(date)
                        }
                        showPaymentDatePicker = false
                    }) { Text("Aceptar") }
                },
                dismissButton = {
                    TextButton(onClick = { showPaymentDatePicker = false }) { Text("Cancelar") }
                }
            ) { DatePicker(state = paymentDatePickerState) }
        }

        // --- DIÁLOGO FECHA DE CREACIÓN ---
        if (showCreationDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showCreationDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        creationDatePickerState.selectedDateMillis?.let { date ->
                            viewModel.onCreationDateChanged(date)
                        }
                        showCreationDatePicker = false
                    }) { Text("Aceptar") }
                },
                dismissButton = {
                    TextButton(onClick = { showCreationDatePicker = false }) { Text("Cancelar") }
                }
            ) { DatePicker(state = creationDatePickerState) }
        }

        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
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

                // 1. Selector de Cliente
                Column(modifier = Modifier.fillMaxWidth()) {
                    CustomerDropdown(
                        customers = state.customers,
                        selectedCustomer = state.selectedCustomer,
                        onCustomerSelected = { viewModel.onCustomerSelected(it) }
                    )
                    if (state.customerError != null) {
                        Text(
                            text = state.customerError!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                        )
                    }
                }

                // 2. Monto
                Column(modifier = Modifier.fillMaxWidth()) {
                    TextFieldApp(
                        value = state.amount,
                        onValueChange = { viewModel.onAmountChanged(it) },
                        label = "Monto a prestar",
                        imageVector = Icons.Default.AttachMoney
                    )
                    if (state.amountError != null) {
                        Text(
                            text = state.amountError!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                        )
                    }
                }

                // 3. Moneda
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

                // --- SECCIÓN DE FECHAS ---
                Text("Fechas", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)

                // 4. FECHA DE CREACIÓN (EMISIÓN)
                Box(modifier = Modifier.fillMaxWidth()) {
                    TextFieldApp(
                        value = DateMethods.formatDate(state.creationDate),
                        onValueChange = { },
                        label = "Fecha de Emisión",
                        readOnly = true,
                        imageVector = Icons.Default.DateRange,
                        trailingIcon = { Icon(Icons.Default.DateRange, null) }
                    )
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clickable { showCreationDatePicker = true }
                    )
                }

                // 5. FECHA DE VENCIMIENTO
                Box(modifier = Modifier.fillMaxWidth()) {
                    TextFieldApp(
                        value = DateMethods.formatDate(state.paymentDate),
                        onValueChange = { },
                        label = "Fecha de Vencimiento",
                        readOnly = true,
                        imageVector = Icons.Default.Event,
                        trailingIcon = { Icon(Icons.Default.Event, null) }
                    )
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clickable { showPaymentDatePicker = true }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 6. Interés
                TextFieldApp(
                    value = state.interestRate,
                    onValueChange = { viewModel.onInterestChanged(it) },
                    label = "Tasa de Interés (%)",
                    imageVector = Icons.Default.Percent
                )

                // 7. Descripción
                TextFieldApp(
                    value = state.description,
                    onValueChange = { viewModel.onDescriptionChanged(it) },
                    label = "Descripción / Notas",
                    imageVector = Icons.Default.Description
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 8. Botón Guardar
                if (state.isLoading) {
                    CircularProgressIndicator()
                } else {
                    PrimaryButton(
                        text = "Crear Préstamo",
                        onClick = { viewModel.saveLoan() },
                        enabled = !state.isLoading
                    )
                }
            }
        }
    }
}

// ... CustomerDropdown sigue igual ...
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerDropdown(
    customers: List<Customer>,
    selectedCustomer: Customer?,
    onCustomerSelected: (Customer) -> Unit
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