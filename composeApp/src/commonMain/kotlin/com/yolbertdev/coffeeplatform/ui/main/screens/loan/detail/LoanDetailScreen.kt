package com.yolbertdev.coffeeplatform.ui.main.screens.loan.detail

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
import com.yolbertdev.coffeeplatform.domain.model.Loan
import com.yolbertdev.coffeeplatform.ui.components.DetailRow
// StatusBadge ya no es necesario importarlo porque lo haremos dinámico aquí
import com.yolbertdev.coffeeplatform.ui.main.screens.customer.detail.CustomerDetailScreen
import com.yolbertdev.coffeeplatform.ui.theme.Gray200
import com.yolbertdev.coffeeplatform.util.DateMethods
import java.io.File

data class LoanDetailScreen(val loan: Loan, val customer: Customer) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        // Lógica de Estado y Progreso
        val isPaid = loan.statusId == 2
        val progress = if (loan.quantity > 0) (loan.paid / loan.quantity).toFloat() else 0f
        val statusColor = if (isPaid) Color(0xFF4CAF50) else Color(0xFFFF9800) // Verde o Naranja
        val statusText = if (isPaid) "COMPLETADO" else "PENDIENTE"

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Detalle del Préstamo", style = MaterialTheme.typography.titleMedium) },
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
                // SECCIÓN 1: RESUMEN DEL MONTO Y ESTADO
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
                        // Badge Dinámico
                        Surface(
                            color = statusColor,
                            shape = RoundedCornerShape(50),
                            modifier = Modifier.padding(bottom = 16.dp)
                        ) {
                            Text(
                                text = statusText,
                                color = Color.White,
                                style = MaterialTheme.typography.labelMedium,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Text(
                            text = "${loan.quantity} ${loan.paymentType}",
                            style = MaterialTheme.typography.displaySmall.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                        Text(
                            text = "Monto total del préstamo",
                            style = MaterialTheme.typography.bodyMedium.copy(color = Gray200)
                        )

                        Spacer(Modifier.height(24.dp))

                        // Barra de Progreso
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Progreso de pago",
                                    style = MaterialTheme.typography.bodySmall.copy(color = Gray200)
                                )
                                Text(
                                    text = "${(progress * 100).toInt()}%",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Bold,
                                    color = statusColor
                                )
                            }
                            Spacer(Modifier.height(8.dp))
                            LinearProgressIndicator(
                                progress = { progress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(10.dp)
                                    .clip(RoundedCornerShape(5.dp)),
                                color = statusColor,
                                trackColor = MaterialTheme.colorScheme.surface
                            )
                            Spacer(Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Pagado: ${loan.paid}",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = statusColor
                                )
                                Text(
                                    text = "Restante: ${loan.quantity - loan.paid}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }

                // SECCIÓN 2: INFORMACIÓN DEL CLIENTE (CARD CLICKABLE)
                Text(
                    text = "Cliente asociado",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navigator.push(CustomerDetailScreen(customer.id)) },
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    shadowElevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val model = if (customer.photo.isNotEmpty()) File(customer.photo) else null

                        AsyncImage(
                            model = model,
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

                // SECCIÓN 3: DETALLES TÉCNICOS
                Text(
                    text = "Información detallada",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    shadowElevation = 2.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        DetailRow(Icons.Rounded.Description, "Descripción", loan.description)
                        DetailRow(Icons.Rounded.Percent, "Tasa de interés", "${loan.interestRate}%")
                        DetailRow(
                            Icons.Rounded.Event,
                            "Fecha de creación",
                            DateMethods.formatDate(loan.creationDate)
                        )
                        DetailRow(
                            Icons.Rounded.CalendarMonth,
                            "Fecha límite de pago",
                            DateMethods.formatDate(loan.paymentDate)
                        )
                        DetailRow(Icons.Rounded.Paid, "Cantidad pagada", "${loan.paid} ${loan.paymentType}")
                    }
                }

                Spacer(Modifier.height(16.dp))
            }
        }
    }
}