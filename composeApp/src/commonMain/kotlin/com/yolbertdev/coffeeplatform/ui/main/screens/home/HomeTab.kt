package com.yolbertdev.coffeeplatform.ui.main.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultShadowColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import coffeeplatform.composeapp.generated.resources.Res
import coffeeplatform.composeapp.generated.resources.coffee_logo
import coffeeplatform.composeapp.generated.resources.dollar
import coffeeplatform.composeapp.generated.resources.home
import coffeeplatform.composeapp.generated.resources.notification
import com.yolbertdev.coffeeplatform.ui.components.MainPaymentItem
import com.yolbertdev.coffeeplatform.ui.theme.Brown500
import com.yolbertdev.coffeeplatform.ui.theme.Brown500_10
import org.jetbrains.compose.resources.painterResource

object HomeTab : Tab {

    override val options: TabOptions
        @Composable
        get() {
            val icon = painterResource(Res.drawable.home)
            return remember {
                TabOptions(
                    index = 0u,
                    title = "Inicio",
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        val screenModel = getScreenModel<HomeScreenModel>()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(32.dp))

            // Header: Saludo y Notificaciones
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Hola, Yolbert!",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        "Bienvenido de vuelta",
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                    )
                }
                Spacer(Modifier.weight(1f))
                Surface(
                    modifier = Modifier.shadow(
                        2.dp, shape = RoundedCornerShape(12.dp),
                        ambientColor = DefaultShadowColor.copy(0.1f)
                    ),
                    color = Color.White,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    IconButton(
                        onClick = {},
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.notification),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // Tarjeta de Saldo Total
            Surface(
                modifier = Modifier.shadow(
                    3.dp, shape = RoundedCornerShape(16.dp),
                    ambientColor = DefaultShadowColor.copy(0.1f)
                ),
                color = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        "Saldo total prestado",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.Black
                    )
                    Spacer(Modifier.height(16.dp))

                    // Fila para Quintales (Qt) - Color Café/Marrón
                    BalanceRow(
                        icon = Res.drawable.coffee_logo,
                        amount = "5,000 Qt",
                        containerColor = Brown500_10, // Marrón café muy suave
                        contentColor = Brown500 // Marrón oscuro para el texto e icono
                    )

                    Spacer(Modifier.height(12.dp))

                    // Fila para Dólares ($) - Color Verde Éxito
                    BalanceRow(
                        icon = Res.drawable.dollar,
                        amount = "1,340.00 $",
                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), // Verde de tu app suave
                        contentColor = MaterialTheme.colorScheme.primary // Verde de tu app sólido
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            Text(
                "Préstamos recientes",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Color.Black
            )

            Spacer(Modifier.height(12.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(5) {
                    MainPaymentItem(
                        customerNickname = "Roberto",
                        customerName = "Roberto Cuji",
                        customerPhoto = "", // Aquí iría la URL o Path
                        amount = "2 Qt",
                        date = "Hoy, 10:30 AM",
                        onClick = {}
                    )
                }
                item {
                    Spacer(Modifier.height(30.dp))
                }
            }
        }
    }

    @Composable
    private fun BalanceRow(
        icon: org.jetbrains.compose.resources.DrawableResource,
        amount: String,
        containerColor: Color,
        contentColor: Color
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(containerColor)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(28.dp)
            )
            Text(
                amount,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                color = contentColor
            )
        }
    }
}