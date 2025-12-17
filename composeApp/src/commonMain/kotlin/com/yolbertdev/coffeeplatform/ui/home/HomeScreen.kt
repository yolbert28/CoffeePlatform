package com.yolbertdev.coffeeplatform.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import coffeeplatform.composeapp.generated.resources.Res
import coffeeplatform.composeapp.generated.resources.notification
import com.yolbertdev.coffeeplatform.ui.components.navigation.NavLayout
import org.jetbrains.compose.resources.painterResource

class HomeScreen : Screen {
    @Composable
    override fun Content() {
        NavLayout {
            Column {
                Row {
                    Text("Hola", style = MaterialTheme.typography.bodyLarge)
                    FilledIconButton(
                        onClick = {},
                        enabled = true,
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(painter = painterResource(Res.drawable.notification), null)
                    }
                }
            }
        }
    }
}