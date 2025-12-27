package com.yolbertdev.coffeeplatform.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultShadowColor
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.yolbertdev.coffeeplatform.domain.Loan
import com.yolbertdev.coffeeplatform.ui.theme.Yellow500
import com.yolbertdev.coffeeplatform.ui.theme.Yellow500_88

@Composable
fun LoanItem(loan: Loan) {
    Column(
        modifier = Modifier.fillMaxWidth().shadow(
            5.dp, shape = RoundedCornerShape(10.dp),
            ambientColor = DefaultShadowColor.copy(0.2f),
            spotColor = DefaultShadowColor.copy(0.2f)
        ).clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).padding(end = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                "Descripción:",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )
            StatusIndicator()
        }
        Spacer(Modifier.height(4.dp))
        Text(
            text = loan.description,
            maxLines = 3, overflow = TextOverflow.Ellipsis
        )
        Spacer(Modifier.height(8.dp))
        ListItemFormatRow("Tasa:", loan.paymentType)
        Spacer(Modifier.height(8.dp))
        ListItemFormatRow("Cantidad:", loan.quantity.toString())
        Spacer(Modifier.height(8.dp))
        ListItemFormatRow("Interes:", (loan.interestRate * 100).toString() + "%")
        Spacer(Modifier.height(8.dp))
        ListItemFormatRow("Fecha de pago:", loan.paymentDate)
        Spacer(Modifier.height(8.dp))
        ListItemFormatRow("Fecha de creación:", loan.creationDate)
    }
}

@Composable
fun StatusIndicator() {
    Box(
        modifier = Modifier.clip(
            CircleShape
        ).background(
            Yellow500_88
        ).border(2.dp, shape = CircleShape, color = Yellow500)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            "Pendiente",
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF753F0D),
        )
    }
}

@Composable
fun ListItemFormatRow(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyLarge,
    titleColor: Color = Color.Black,
    valueColor: Color = Color.Black
) {
    Text(
        text = buildAnnotatedString {
            withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = titleColor)) {
                append("$title ")
            }
            withStyle(SpanStyle(fontWeight = FontWeight.Normal, color = valueColor)) {
                append(value)
            }
        },
        modifier = modifier,
        style = style,
        color = titleColor
    )
}