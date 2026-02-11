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
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Payments
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
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.AsyncImage
import com.yolbertdev.coffeeplatform.domain.model.Customer
import com.yolbertdev.coffeeplatform.ui.components.ListItemFormatRow
import com.yolbertdev.coffeeplatform.ui.components.LoanItem
import com.yolbertdev.coffeeplatform.ui.components.MainPaymentItem
import com.yolbertdev.coffeeplatform.ui.components.PaymentItem
import com.yolbertdev.coffeeplatform.ui.components.SearchBarApp
import com.yolbertdev.coffeeplatform.ui.main.screens.customer.edit.EditCustomerScreen
import com.yolbertdev.coffeeplatform.ui.main.screens.loan.detail.LoanDetailScreen
import com.yolbertdev.coffeeplatform.ui.theme.Gray200
import com.yolbertdev.coffeeplatform.util.DateMethods
import java.io.File

enum class CustomerDetailSection(val title: String, val icon: ImageVector) {
    INFO("Información", Icons.Rounded.Info),
    LOANS("Préstamos", Icons.AutoMirrored.Rounded.ReceiptLong),
    PAYMENTS("Pagos", Icons.Rounded.Payments)
}

data class CustomerDetailScreen(val customer: Customer) : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        // Inyección del ViewModel
        val viewModel = getScreenModel<CustomerDetailScreenModel>()
        val state by viewModel.uiState.collectAsState()

        // Efecto lanzado una sola vez para cargar los datos
        LaunchedEffect(customer.id) {
            viewModel.loadData(customer.id)
        }

        var selectedSection by remember { mutableStateOf(CustomerDetailSection.INFO) }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Rounded.ArrowBackIosNew,
                                contentDescription = "Regresar",
                                modifier = Modifier
                                    .size(24.dp)
                                    .clickable { navigator.pop() }
                            )
                            Spacer(Modifier.width(16.dp))
                            Text(
                                text = customer.name,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                )
            }
        ) { padding ->
            Column(modifier = Modifier.padding(padding)) {
                // Cabecera fija (Foto y nombres)
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Manejo seguro de la imagen local
                    val model = if (customer.photo.isNotEmpty() && File(customer.photo).exists()) {
                        File(customer.photo)
                    } else {
                        // Un placeholder o recurso por defecto si no hay foto
                        // Res.drawable.default_avatar
                        null
                    }

                    AsyncImage(
                        model = model,
                        contentDescription = customer.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(Gray200) // Fondo gris mientras carga o si es null
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = customer.nickname,
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = customer.name,
                        style = MaterialTheme.typography.bodyMedium.copy(color = Gray200)
                    )
                    Spacer(Modifier.height(16.dp))

                    OutlinedButton(
                        onClick = { navigator.push(EditCustomerScreen(customer))},
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth(0.6f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(Icons.Rounded.Edit, null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Editar Perfil", style = MaterialTheme.typography.labelLarge)
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
                            text = {
                                Text(
                                    text = section.title,
                                    style = MaterialTheme.typography.labelLarge
                                )
                            },
                            icon = {
                                Icon(
                                    imageVector = section.icon,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        )
                    }
                }

                // Contenido Dinámico con LazyColumn
                if (state.isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                        contentPadding = PaddingValues(vertical = 16.dp)
                    ) {
                        when (selectedSection) {
                            CustomerDetailSection.INFO -> {
                                item {
                                    InfoSection(customer)
                                }
                            }

                            CustomerDetailSection.LOANS -> {
                                item {
                                    // Podrías mover el estado del search al ViewModel si quieres filtrar la lista 'state.loans'
                                    var searchQuery by remember { mutableStateOf("") }
                                    SearchBarApp(
                                        value = searchQuery,
                                        onValueChange = { searchQuery = it },
                                        placeholder = "Buscar préstamos..."
                                    )
                                    Spacer(Modifier.height(8.dp))
                                }

                                // Renderizar lista real
                                if (state.loans.isEmpty()) {
                                    item {
                                        Text("No hay préstamos registrados", modifier = Modifier.padding(16.dp))
                                    }
                                } else {
                                    items(state.loans) { loan ->
                                        LoanItem(loan = loan, onClick = { }, customerName = customer.name)
                                        Spacer(Modifier.height(8.dp))
                                    }
                                }
                            }

                            CustomerDetailSection.PAYMENTS -> {
                                // Aquí iría la lógica similar para pagos cuando tengas el repositorio
                                item {
                                    Text("Historial de pagos", style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(top = 8.dp))
                                }
                                if (state.isLoading) {
                                    item {
                                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                            CircularProgressIndicator()
                                        }
                                    }
                                } else if (state.payments.isEmpty()) {
                                    item {
                                        Text(
                                            "No hay pagos registrados para este cliente.",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.outline,
                                            modifier = Modifier.padding(vertical = 8.dp)
                                        )
                                    }
                                } else {
                                    // Lista de Pagos
                                    items(state.payments) { payment ->
                                        MainPaymentItem(
                                            customerNickname = "", // No necesario en detalle
                                            customerName = "Pago realizado", // Título genérico o nota
                                            customerPhoto = "", // No mostramos foto repetida
                                            amount = "${payment.amount} ${payment.paymentType}",
                                            date = DateMethods.formatDate(payment.creationDate),
                                            onClick = { /* Opcional: ver detalle del pago */ }
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
            ListItemFormatRow(
                title = "Descripción:",
                value = customer.description,
                style = MaterialTheme.typography.bodyLarge
            )
            ListItemFormatRow(
                title = "Nivel de crédito:",
                value = customer.creditLevel.toString(),
                style = MaterialTheme.typography.bodyLarge
            )
            ListItemFormatRow(
                title = "Ubicación:",
                value = customer.location,
                style = MaterialTheme.typography.bodyLarge
            )
            // Puedes agregar más campos aquí, como fecha de registro, etc.
        }
    }
}