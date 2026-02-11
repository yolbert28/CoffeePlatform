package com.yolbertdev.coffeeplatform.ui.main.screens.payment.add

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.yolbertdev.coffeeplatform.domain.repository.LoanWithCustomerName
import com.yolbertdev.coffeeplatform.ui.components.PrimaryButton
import com.yolbertdev.coffeeplatform.ui.components.TextFieldApp
import com.yolbertdev.coffeeplatform.util.DateMethods
import kotlinx.coroutines.delay

class AddPaymentScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<AddPaymentScreenModel>()
        val state by viewModel.state.collectAsState()

        val snackbarHostState = remember { SnackbarHostState() }
        var showDatePicker by remember { mutableStateOf(false) }
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = state.paymentDate)

        // Manejo del éxito
        LaunchedEffect(state.success) {
            if (state.success) {
                snackbarHostState.showSnackbar("Pago registrado exitosamente")
                delay(700)
                viewModel.onHandled()
                navigator.pop()
            }
        }

        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        datePickerState.selectedDateMillis?.let { viewModel.onDateChanged(it) }
                        showDatePicker = false
                    }) { Text("Aceptar") }
                }
            ) { DatePicker(state = datePickerState) }
        }

        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                TopAppBar(
                    title = { Text("Registrar Pago") },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(Icons.Rounded.ArrowBackIosNew, "Atrás")
                        }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 1. Dropdown para seleccionar préstamo
                LoanDropdown(
                    loans = state.pendingLoans,
                    selectedLoan = state.selectedLoanWrapper,
                    onLoanSelected = { viewModel.onLoanSelected(it) }
                )

                // Tarjeta de Información de Deuda
                if (state.selectedLoanWrapper != null) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Deuda Restante", style = MaterialTheme.typography.labelMedium)
                            Text(
                                "${state.remainingAmount} ${state.currency}",
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                // 2. Campo Monto
                TextFieldApp(
                    value = state.amount,
                    onValueChange = { viewModel.onAmountChanged(it) },
                    label = "Monto a abonar",
                    imageVector = Icons.Default.AttachMoney,
                    trailingIcon = {
                        if(state.currency.isNotEmpty()) Text(state.currency, modifier = Modifier.padding(end = 8.dp))
                    }
                )

                // 3. Campo Fecha
                Box(modifier = Modifier.fillMaxWidth()) {
                    TextFieldApp(
                        value = DateMethods.formatDate(state.paymentDate),
                        onValueChange = {},
                        label = "Fecha del Pago",
                        readOnly = true,
                        imageVector = Icons.Default.CalendarMonth
                    )
                    // Capa invisible para detectar el clic
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clickable { showDatePicker = true }
                    )
                }

                // 4. Campo Nota
                TextFieldApp(
                    value = state.note,
                    onValueChange = { viewModel.onNoteChanged(it) },
                    label = "Nota (Efectivo / Trueque)",
                    imageVector = Icons.Default.Description
                )

                if (state.error != null) {
                    Text(state.error!!, color = MaterialTheme.colorScheme.error)
                }

                Spacer(Modifier.height(8.dp))

                if (state.isLoading) {
                    CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
                } else {
                    PrimaryButton(
                        text = "Guardar Pago",
                        onClick = { viewModel.savePayment() },
                        enabled = state.selectedLoanWrapper != null
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoanDropdown(
    loans: List<LoanWithCustomerName>,
    selectedLoan: LoanWithCustomerName?,
    onLoanSelected: (LoanWithCustomerName) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedLoan?.let { "${it.customerName} - ${it.loan.quantity} ${it.currency}" } ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text("Seleccionar Préstamo Pendiente") },
            placeholder = { Text("Busca por cliente...") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.fillMaxWidth().menuAnchor(),
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            if (loans.isEmpty()) {
                DropdownMenuItem(
                    text = { Text("No hay préstamos pendientes") },
                    onClick = { expanded = false }
                )
            } else {
                loans.forEach { wrapper ->
                    val remaining = wrapper.loan.quantity - wrapper.loan.paid
                    DropdownMenuItem(
                        text = {
                            Column {
                                Text(wrapper.customerName, style = MaterialTheme.typography.bodyLarge)
                                Text(
                                    "Deuda: $remaining ${wrapper.currency}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.outline
                                )
                            }
                        },
                        onClick = {
                            onLoanSelected(wrapper)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}