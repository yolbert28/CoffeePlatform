package com.yolbertdev.coffeeplatform.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coffeeplatform.composeapp.generated.resources.Res
import coffeeplatform.composeapp.generated.resources.close
import com.yolbertdev.coffeeplatform.ui.theme.Gray200
import org.jetbrains.compose.resources.painterResource

@Composable
fun SelectedFilterItem(
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.clip(CircleShape)
            .background(MaterialTheme.colorScheme.surface).border(
                2.dp,
                MaterialTheme.colorScheme.outline,
                CircleShape
            ).padding(horizontal = 16.dp).clickable{ onClick() },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            color = Gray200,
            modifier = Modifier.padding(
                vertical = 8.dp
            )
        )
        Spacer(Modifier.width(8.dp))
        Icon(painter = painterResource(Res.drawable.close), contentDescription = null, modifier = Modifier.size(12.dp), tint = Gray200)
    }
}