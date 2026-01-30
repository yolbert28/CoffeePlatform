package com.yolbertdev.coffeeplatform.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.yolbertdev.coffeeplatform.domain.model.Loan
import com.yolbertdev.coffeeplatform.ui.theme.Gray200
import com.yolbertdev.coffeeplatform.ui.theme.Yellow300
import com.yolbertdev.coffeeplatform.ui.theme.Yellow500
// 1. IMPORTANTE: Agregar este import
import com.yolbertdev.coffeeplatform.util.DateMethods

@Composable
fun LoanItem(loan: Loan, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable{ onClick() },
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // CABECERA: Monto y Estado
            Column {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Monto del préstamo",
                        style = MaterialTheme.typography.labelMedium.copy(color = Gray200)
                    )
                    StatusBadge(status = "Pendiente") // Aquí podrías mapear el statusId
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "${loan.quantity}",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    ),
                    overflow = TextOverflow.Ellipsis
                )
            }


            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            )

            // DESCRIPCIÓN
            if (loan.description.isNotEmpty()) {
                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        Icons.Rounded.Notes,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp).padding(top = 2.dp),
                        tint = Gray200
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = loan.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(Modifier.height(12.dp))
            }

            // DETALLES EN GRID (2 columnas)
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    LoanDetailInfo(
                        Icons.Rounded.Percent,
                        "Interés",
                        "${(loan.interestRate)}%" // Eliminé el *100 si ya lo guardas como entero, ajusta según tu lógica
                    )
                    Spacer(Modifier.height(8.dp))

                    // 2. CORRECCIÓN: Usar DateMethods.formatDate para convertir Long -> String
                    LoanDetailInfo(
                        Icons.Rounded.Event,
                        "Creación",
                        DateMethods.formatDate(loan.creationDate)
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    LoanDetailInfo(Icons.Rounded.Payments, "Tasa", loan.paymentType)
                    Spacer(Modifier.height(8.dp))

                    // 3. CORRECCIÓN: Usar DateMethods.formatDate
                    LoanDetailInfo(
                        Icons.Rounded.CalendarMonth,
                        "Pago",
                        DateMethods.formatDate(loan.paymentDate)
                    )
                }
            }
        }
    }
}

@Composable
private fun LoanDetailInfo(icon: ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(14.dp),
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
        )
        Spacer(Modifier.width(6.dp))
        Column {
            Text(text = label, style = MaterialTheme.typography.labelSmall.copy(color = Gray200))
            Text(
                text = value,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}

@Composable
fun StatusBadge(status: String) {
    Surface(
        shape = CircleShape,
        color = Yellow500.copy(alpha = 0.15f),
        contentColor = Yellow300
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(Yellow500)
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text = status,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}