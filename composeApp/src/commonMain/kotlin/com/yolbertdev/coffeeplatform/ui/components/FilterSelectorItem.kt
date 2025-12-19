package com.yolbertdev.coffeeplatform.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yolbertdev.coffeeplatform.ui.theme.Gray700

@Composable
fun FilterSelectorItem(
    text: String
) {
    Box(
        modifier = Modifier.height(28.dp).clip(CircleShape)
            .background(Gray700).border(
                2.dp,
                MaterialTheme.colorScheme.outlineVariant,
                CircleShape
            )
    ) {
        Text(
            text = text,
            style = TextStyle(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp).align(
                Alignment.Center)
        )
    }
}