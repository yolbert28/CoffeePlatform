package com.yolbertdev.coffeeplatform.ui.main.screens.payment.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.AsyncImage
import com.yolbertdev.coffeeplatform.domain.model.Customer
import com.yolbertdev.coffeeplatform.ui.components.DetailRow
import com.yolbertdev.coffeeplatform.ui.main.screens.customer.detail.CustomerDetailScreen
import com.yolbertdev.coffeeplatform.ui.theme.Gray200

// Nota: He usado tipos genéricos para amount y date ya que Payment aún no tiene un data class formal en domain/model
data class PaymentDetailScreen(
    val amount: String,
    val paymentType: String,
    val date: String,
    val customer: Customer
) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Detalle del Pago", style = MaterialTheme.typography.titleMedium) },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(Icons.Rounded.ArrowBackIosNew, contentDescription = "Regresar")
                        }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // SECCIÓN 1: RESUMEN DEL PAGO (MONTO)
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                            modifier = Modifier.size(64.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Rounded.CheckCircle,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                        Spacer(Modifier.height(16.dp))
                        Text(
                            text = amount,
                            style = MaterialTheme.typography.displaySmall.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                        Text(
                            text = "Pago realizado exitosamente",
                            style = MaterialTheme.typography.bodyMedium.copy(color = Gray200)
                        )
                    }
                }

                // SECCIÓN 2: INFORMACIÓN DEL CLIENTE (CARD CLICKABLE)
                Text(
                    text = "Cliente beneficiario",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navigator.push(CustomerDetailScreen(customer)) },
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    shadowElevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = customer.photo,
                            contentDescription = customer.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer)
                        )
                        Spacer(Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = customer.name,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "@${customer.nickname}",
                                style = MaterialTheme.typography.bodySmall.copy(color = Gray200)
                            )
                        }
                        Icon(
                            imageVector = Icons.Rounded.ChevronRight,
                            contentDescription = null,
                            tint = Gray200
                        )
                    }
                }

                // SECCIÓN 3: DETALLES DEL PAGO
                Text(
                    text = "Detalles de la transacción",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    shadowElevation = 2.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        DetailRow(Icons.Rounded.Payments, "Método de pago", paymentType)
                        DetailRow(Icons.Rounded.CalendarMonth, "Fecha del pago", date)
                        DetailRow(Icons.Rounded.Receipt, "ID de transacción", "#PAY-${(1000..9999).random()}")
                    }
                }
                
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}
