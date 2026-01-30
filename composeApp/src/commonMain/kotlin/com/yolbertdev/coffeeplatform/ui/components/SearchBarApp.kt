package com.yolbertdev.coffeeplatform.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.DefaultShadowColor
import androidx.compose.ui.unit.dp
import coffeeplatform.composeapp.generated.resources.Res
import coffeeplatform.composeapp.generated.resources.search
import org.jetbrains.compose.resources.painterResource

@Composable
fun SearchBarApp(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "Buscar...",
    onSearchClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 3.dp,
                shape = RoundedCornerShape(12.dp),
                ambientColor = DefaultShadowColor.copy(0.1f)
            ),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        TextFieldApp(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            },
            trailingIcon = {
                IconButton(
                    onClick = onSearchClick,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.search),
                        contentDescription = "Buscar",
                        modifier = Modifier.size(24.dp)
                    )
                }
            },
        )
    }
}
