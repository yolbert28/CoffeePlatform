package com.yolbertdev.coffeeplatform.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

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