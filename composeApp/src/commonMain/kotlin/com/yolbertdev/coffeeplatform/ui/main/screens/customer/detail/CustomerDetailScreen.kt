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
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Payments
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.AsyncImage
import com.yolbertdev.coffeeplatform.domain.model.Customer
import com.yolbertdev.coffeeplatform.domain.model.Loan
import com.yolbertdev.coffeeplatform.ui.components.ListItemFormatRow
import com.yolbertdev.coffeeplatform.ui.components.LoanItem
import com.yolbertdev.coffeeplatform.ui.components.PaymentItem
import com.yolbertdev.coffeeplatform.ui.components.TextFieldApp
import com.yolbertdev.coffeeplatform.ui.theme.Gray200

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
        var selectedSection by remember { mutableStateOf(CustomerDetailSection.INFO) }
        var searchQuery by remember { mutableStateOf("") }

        // Mock data para préstamos (esto debería venir de un ScreenModel)
        val mockLoan = Loan(
            id = 1,
            customerId = 1,
            interestRate = 0.125,
            description = "Préstamo de prueba",
            paymentDate = "12/03/2024",
            paymentType = "Dólares",
            quantity = 1000.0,
            paid = 0.0,
            statusId = 1,
            creationDate = "12/12/2023",
            updateDate = "12/10/2023"
        )

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
                    AsyncImage(
                        model = customer.photo,
                        contentDescription = customer.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
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
                }

                // Selector de Secciones (Tipo Instagram)
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

                // Contenido Dinámico
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
                                Box(
                                    modifier = Modifier.padding(bottom = 12.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(MaterialTheme.colorScheme.surface)
                                        .padding(12.dp)
                                ) {
                                    TextFieldApp(
                                        value = searchQuery,
                                        onValueChange = { searchQuery = it },
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        placeholder = { Text("Buscar préstamo...") },
                                        trailingIcon = {
                                            Icon(
                                                imageVector = Icons.Rounded.Search,
                                                contentDescription = null
                                            )
                                        }
                                    )
                                }

                            }
                            // Aquí irían los préstamos reales del cliente
                            items(5) {
                                LoanItem(mockLoan)
                                Spacer(Modifier.height(8.dp))
                            }
                        }

                        CustomerDetailSection.PAYMENTS -> {
                            items(3) { index ->
                                PaymentItem(
                                    amount = 50.0 + (index * 10),
                                    paymentType = if (index % 2 == 0) "Efectivo" else "Transferencia",
                                    date = "2${index}/05/2024"
                                )
                                Spacer(Modifier.height(8.dp))
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
        }
    }
}
