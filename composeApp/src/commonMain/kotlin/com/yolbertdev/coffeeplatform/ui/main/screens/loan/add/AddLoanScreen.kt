package com.yolbertdev.coffeeplatform.ui.main.screens.loan.add

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Event
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
import kotlinx.coroutines.delay
import com.yolbertdev.coffeeplatform.util.DateMethods
class AddLoanScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<AddLoanScreenModel>()
        val state by viewModel.uiState.collectAsState()

        // Estado para el Snackbar
        val snackbarHostState = remember { SnackbarHostState() }
        var showDatePicker by remember { mutableStateOf(false) }
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = state.paymentDate
        )

        // Efecto para manejar el éxito del guardado
        LaunchedEffect(state.showSuccessMessage) {
            if (state.showSuccessMessage) {
                snackbarHostState.showSnackbar("¡Préstamo creado correctamente!")
                delay(1000) // Espera un poco para que el usuario lea el mensaje
                viewModel.onNavigationHandled()
                navigator.pop()
            }
        }
        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        // AQUÍ CAPTURAMOS LA FECHA SELECCIONADA
                        datePickerState.selectedDateMillis?.let { date ->
                            // Truco opcional: Sumar 12 horas (43200000ms) para evitar problemas de zona horaria
                            // y que caiga al día anterior si estás en UTC-4.
                            // O simplemente pasar 'date' tal cual si DateMethods maneja UTC.
                            // Por ahora pasamos 'date' directo, asumiendo que solo quieres guardar el timestamp.
                            viewModel.onPaymentDateChanged(date)
                        }
                        showDatePicker = false
                    }) {
                        Text("Aceptar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text("Cancelar")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) }, // Agregamos el Host
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
                    // Mensaje de error para Cliente
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
                    // Mensaje de error para Monto
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
                Box(modifier = Modifier.fillMaxWidth()) {
                    TextFieldApp(
                        value = DateMethods.formatDate(state.paymentDate), // Muestra lo que hay en el State
                        onValueChange = { },
                        label = "Fecha de Vencimiento",
                        readOnly = true,
                        imageVector = Icons.Default.Event, // Icono de calendario
                        trailingIcon = { Icon(Icons.Default.Event, null) }
                    )
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clickable { showDatePicker = true } // Abre el calendario
                    )
                }
                // 4. Interés
                TextFieldApp(
                    value = state.interestRate,
                    onValueChange = { viewModel.onInterestChanged(it) },
                    label = "Tasa de Interés (%)",
                    imageVector = Icons.Default.Percent
                )
                Box(modifier = Modifier.fillMaxWidth()) {
                    TextFieldApp(
                        value = DateMethods.formatDate(state.paymentDate), // Formateamos la fecha
                        onValueChange = { }, // No hace nada manual
                        label = "Fecha de Vencimiento",
                        readOnly = true, // No editable manualmente
                        imageVector = Icons.Default.CalendarMonth,
                        trailingIcon = {
                            Icon(Icons.Default.CalendarMonth, contentDescription = null)
                        }
                    )
                    // Capa invisible clickable
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clickable { showDatePicker = true }
                    )
                }
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
                        onClick = { viewModel.saveLoan() }, // Ya no pasamos lambda, el ViewModel maneja estado
                        // Deshabilitamos validación simple aquí para dejar que el ViewModel muestre los errores detallados
                        enabled = !state.isLoading
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