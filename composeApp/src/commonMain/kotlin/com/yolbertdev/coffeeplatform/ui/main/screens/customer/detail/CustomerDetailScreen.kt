package com.yolbertdev.coffeeplatform.ui.main.screens.customer.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ReceiptLong
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.AsyncImage
import com.yolbertdev.coffeeplatform.domain.model.Customer
import com.yolbertdev.coffeeplatform.ui.components.ListItemFormatRow
import com.yolbertdev.coffeeplatform.ui.components.LoanItem
import com.yolbertdev.coffeeplatform.ui.components.PaymentItem
import com.yolbertdev.coffeeplatform.ui.components.SearchBarApp
import com.yolbertdev.coffeeplatform.ui.main.screens.customer.edit.EditCustomerScreen
import com.yolbertdev.coffeeplatform.ui.theme.Gray200
import com.yolbertdev.coffeeplatform.util.DateMethods

enum class CustomerDetailSection(val title: String, val icon: ImageVector) {
    INFO("Información", Icons.Rounded.Info),
    LOANS("Préstamos", Icons.AutoMirrored.Rounded.ReceiptLong),
    PAYMENTS("Pagos", Icons.Rounded.Payments)
}

data class CustomerDetailScreen(val customerId: Long) : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinScreenModel<CustomerDetailScreenModel>()
        val state by viewModel.uiState.collectAsState()

        // Disparamos la carga cada vez que el ID cambie o la pantalla se componga
        LaunchedEffect(customerId) {
            viewModel.loadData(customerId)
        }

        var selectedSection by remember { mutableStateOf(CustomerDetailSection.INFO) }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = state.customer?.name ?: "Detalles",
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(Icons.Rounded.ArrowBackIosNew, "Regresar")
                        }
                    }
                )
            }
        ) { padding ->
            // MOSTRAR LOADING GLOBAL SI ESTÁ CARGANDO
            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (state.customer != null) {
                Column(modifier = Modifier.padding(padding)) {
                    // Cabecera fija (Foto y nombres)
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            if (!state.customer?.photo.isNullOrEmpty()) {
                                AsyncImage(
                                    model = state.customer?.photo,
                                    contentDescription = state.customer?.name,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                Icon(
                                    Icons.Rounded.Person,
                                    null,
                                    modifier = Modifier.size(64.dp),
                                    tint = Gray200
                                )
                            }
                        }
                        
                        Spacer(Modifier.height(12.dp))
                        Text(
                            text = state.customer?.nickname ?: "",
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = state.customer?.name ?: "",
                            style = MaterialTheme.typography.bodyMedium.copy(color = Gray200)
                        )
                        Spacer(Modifier.height(16.dp))

                        OutlinedButton(
                            onClick = {
                                navigator.push(EditCustomerScreen(state.customer!!))
                            },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth(0.6f)
                        ) {
                            Icon(Icons.Rounded.Edit, null, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("Editar Perfil")
                        }
                    }

                    // Selector de Secciones
                    SecondaryTabRow(
                        selectedTabIndex = selectedSection.ordinal,
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.primary
                    ) {
                        CustomerDetailSection.entries.forEach { section ->
                            Tab(
                                selected = selectedSection == section,
                                onClick = { selectedSection = section },
                                text = { Text(section.title) },
                                icon = { Icon(section.icon, null, modifier = Modifier.size(20.dp)) }
                            )
                        }
                    }

                    // Contenido Dinámico
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                        contentPadding = PaddingValues(vertical = 16.dp)
                    ) {
                        when (selectedSection) {
                            CustomerDetailSection.INFO -> {
                                item { InfoSection(state.customer!!) }
                            }

                            CustomerDetailSection.LOANS -> {
                                if (state.loans.isEmpty()) {
                                    item { EmptyState("No hay préstamos registrados") }
                                } else {
                                    items(state.loans) { loan ->
                                        LoanItem(
                                            loan = loan,
                                            onClick = { },
                                            customerName = state.customer?.name ?: ""
                                        )
                                        Spacer(Modifier.height(8.dp))
                                    }
                                }
                            }

                            CustomerDetailSection.PAYMENTS -> {
                                if (state.payments.isEmpty()) {
                                    item { EmptyState("No hay pagos registrados") }
                                } else {
                                    items(state.payments) { payment ->
                                        PaymentItem(
                                            amount = payment.amount,
                                            paymentType = payment.paymentType,
                                            date = DateMethods.formatDate(payment.creationDate),
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun InfoSection(customer: Customer) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            ListItemFormatRow(title = "Descripción:", value = customer.description)
            ListItemFormatRow(title = "Nivel de crédito:", value = customer.creditLevel.toString())
            ListItemFormatRow(title = "Ubicación:", value = customer.location)
            ListItemFormatRow(title = "Cédula:", value = customer.idCard ?: "")
        }
    }

    @Composable
    private fun EmptyState(message: String) {
        Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
            Text(message, style = MaterialTheme.typography.bodyMedium, color = Gray200)
        }
    }
}