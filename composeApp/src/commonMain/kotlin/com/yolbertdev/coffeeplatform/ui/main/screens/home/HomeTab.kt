package com.yolbertdev.coffeeplatform.ui.main.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.yolbertdev.coffeeplatform.util.DateMethods
import org.jetbrains.compose.resources.painterResource

// Definimos los colores aquí para asegurar que el diseño funcione sin tocar Color.kt
val Brown500 = Color(0xFF795548)
val Brown500_10 = Color(0x1A795548) // Alpha 10%

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
        val viewModel = getScreenModel<HomeScreenModel>()
        val state by viewModel.state.collectAsState()

        LaunchedEffect(Unit) {
            viewModel.loadData()
        }

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
                        "Hola, ${state.userName}!", // NOMBRE REAL
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

                    // Fila para Quintales (Qt) - DATO REAL
                    BalanceRow(
                        icon = Res.drawable.coffee_logo,
                        amount = "${state.totalQuintales} Qt",
                        containerColor = Brown500_10,
                        contentColor = Brown500
                    )

                    Spacer(Modifier.height(12.dp))

                    // Fila para Dólares ($) - DATO REAL
                    BalanceRow(
                        icon = Res.drawable.dollar,
                        amount = "${state.totalDollars} $",
                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        contentColor = MaterialTheme.colorScheme.primary
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

            // LISTA REAL
            if (state.isLoading) {
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.recentLoans) { payment ->
//                        MainPaymentItem(payment, // Formatear fecha real
//                            onClick = {
//                                // Navegación al detalle si lo deseas
//                            }
//                        )
                    }
                    item {
                        Spacer(Modifier.height(30.dp))
                    }
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