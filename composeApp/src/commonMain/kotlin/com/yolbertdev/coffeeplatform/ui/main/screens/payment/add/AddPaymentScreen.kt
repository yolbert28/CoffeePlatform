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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.yolbertdev.coffeeplatform.domain.repository.LoanWithCustomerName
import com.yolbertdev.coffeeplatform.ui.components.PrimaryButton
import com.yolbertdev.coffeeplatform.ui.components.SecondaryTextFieldApp
import com.yolbertdev.coffeeplatform.util.DateMethods
import kotlinx.coroutines.delay

class AddPaymentScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinScreenModel<AddPaymentScreenModel>()
        val state by viewModel.state.collectAsState()

        val snackbarHostState = remember { SnackbarHostState() }
        var showDatePicker by remember { mutableStateOf(false) }
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = state.paymentDate,
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    // Si hay un préstamo seleccionado, bloqueamos los días anteriores a su creación
                    val loanDate = state.selectedLoanWrapper?.loan?.creationDate
                    return if (loanDate != null) {
                        utcTimeMillis >= (loanDate - 86_400_000L)
                    } else {
                        true // Si no ha seleccionado préstamo, dejamos el calendario libre
                    }
                }
            }
        )
        val scrollState = rememberScrollState()
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
                    title = { Text("Registrar Pago", style = MaterialTheme.typography.titleMedium) },
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
                    .imePadding()
                    .padding(horizontal = 24.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Spacer(Modifier.height(8.dp))

                LoanDropdown(
                    loans = state.pendingLoans,
                    selectedLoan = state.selectedLoanWrapper,
                    onLoanSelected = { viewModel.onLoanSelected(it) }
                )

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

                FormField(
                    label = "Monto a abonar",
                    value = state.amount,
                    onValueChange = { viewModel.onAmountChanged(it) },
                    icon = Icons.Default.AttachMoney,
                    trailingIcon = {
                        if(state.currency.isNotEmpty()) Text(state.currency, modifier = Modifier.padding(end = 12.dp))
                    }
                )

                Box(modifier = Modifier.fillMaxWidth()) {
                    FormField(
                        label = "Fecha del Pago",
                        value = DateMethods.formatDate(state.paymentDate),
                        onValueChange = {},
                        readOnly = true,
                        icon = Icons.Default.CalendarMonth
                    )
                    Box(modifier = Modifier.matchParentSize().clickable { showDatePicker = true })
                }

                FormField(
                    label = "Nota (Efectivo / Trueque)",
                    value = state.note,
                    onValueChange = { viewModel.onNoteChanged(it) },
                    icon = Icons.Default.Description
                )

                if (state.error != null) {
                    Text(state.error!!, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(start = 12.dp))
                }

                Spacer(Modifier.height(8.dp))

                if (state.isLoading) {
                    CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
                } else {
                    PrimaryButton(
                        text = "Guardar Pago",
                        onClick = { viewModel.savePayment{ navigator.pop() } },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        enabled = state.selectedLoanWrapper != null
                    )
                }
                 Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun FormField(
    label: String,
    icon: ImageVector,
    value: String,
    onValueChange: (String) -> Unit,
    readOnly: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)),
            modifier = Modifier.padding(start = 8.dp, bottom = 4.dp)
        )
        SecondaryTextFieldApp(
            value = value,
            onValueChange = onValueChange,
            readOnly = readOnly,
            placeholder = { Text("Escribir...", color = MaterialTheme.colorScheme.outline, style = MaterialTheme.typography.bodyMedium) },
            leadingIcon = { Icon(icon, null, modifier = Modifier.size(20.dp), tint = MaterialTheme.colorScheme.primary) },
            trailingIcon = trailingIcon
        )
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

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Préstamo Pendiente",
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)),
            modifier = Modifier.padding(start = 8.dp, bottom = 4.dp)
        )
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            SecondaryTextFieldApp(
                value = selectedLoan?.let { "${it.customerName} - ${it.loan.quantity} ${it.currency}" } ?: "",
                onValueChange = {},
                readOnly = true,
                placeholder = { Text("Seleccionar Préstamo", color = MaterialTheme.colorScheme.outline) },
                leadingIcon = { Icon(Icons.Default.Person, null, modifier = Modifier.size(20.dp), tint = MaterialTheme.colorScheme.primary) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                if (loans.isEmpty()) {
                    DropdownMenuItem(text = { Text("No hay préstamos pendientes") }, onClick = { expanded = false })
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
}