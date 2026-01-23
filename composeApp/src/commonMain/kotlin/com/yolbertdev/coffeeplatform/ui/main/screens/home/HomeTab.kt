package com.yolbertdev.coffeeplatform.ui.main.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultShadowColor
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import coffeeplatform.composeapp.generated.resources.Res
import coffeeplatform.composeapp.generated.resources.coffee_logo
import coffeeplatform.composeapp.generated.resources.dollar
import coffeeplatform.composeapp.generated.resources.home
import coffeeplatform.composeapp.generated.resources.notification
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
            modifier = Modifier.Companion.padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.Companion.height(28.dp))
            Row(
                verticalAlignment = Alignment.Companion.CenterVertically
            ) {
                Text("Hola, Yolbert!", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.Companion.weight(1f))
                IconButton(
                    modifier = Modifier.Companion.shadow(
                        5.dp, shape = RoundedCornerShape(10.dp),
                        ambientColor = DefaultShadowColor.copy(0.2f),
                        spotColor = DefaultShadowColor.copy(0.2f)
                    ),
                    onClick = {},
                    enabled = true,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.notification),
                        contentDescription = null,
                        modifier = Modifier.Companion.padding(4.dp).size(28.dp)
                    )
                }
            }
            Spacer(Modifier.Companion.height(24.dp))
            Surface(
                modifier = Modifier.Companion.shadow(
                    5.dp, shape = androidx.compose.foundation.shape.RoundedCornerShape(10.dp),
                    ambientColor = DefaultShadowColor.copy(0.2f),
                    spotColor = DefaultShadowColor.copy(0.2f)
                ).clip(androidx.compose.foundation.shape.RoundedCornerShape(10.dp)),
            ) {
                Column(
                    modifier = Modifier.Companion.padding(16.dp)
                ) {
                    Text(
                        "Saldo total prestado:",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Companion.Black
                    )
                    Spacer(Modifier.Companion.height(12.dp))
                    Row(
                        modifier = Modifier.Companion.fillMaxWidth()
                            .clip(androidx.compose.foundation.shape.RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                            .padding(16.dp),
                        verticalAlignment = Alignment.Companion.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.coffee_logo),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.Companion.size(32.dp)
                        )
                        Text(
                            "5000 Qt",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                    Spacer(Modifier.Companion.height(12.dp))
                    Row(
                        modifier = Modifier.Companion.fillMaxWidth()
                            .clip(androidx.compose.foundation.shape.RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                            .padding(16.dp),
                        verticalAlignment = Alignment.Companion.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.dollar),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.Companion.size(32.dp)
                        )
                        Text(
                            "1340$",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }

            }
            Spacer(Modifier.Companion.height(16.dp))
            Text(
                "Prestamos recientes:",
                style = MaterialTheme.typography.titleSmall,
                color = Color.Companion.Black
            )
            Spacer(Modifier.Companion.height(12.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.Companion.CenterHorizontally
            ) {
                Surface(
                    modifier = Modifier.Companion.shadow(
                        5.dp, shape = androidx.compose.foundation.shape.RoundedCornerShape(10.dp),
                        ambientColor = DefaultShadowColor.copy(0.2f),
                        spotColor = DefaultShadowColor.copy(0.2f)
                    ).clip(androidx.compose.foundation.shape.RoundedCornerShape(10.dp))
                        .fillMaxWidth()

                ) {
                    Row(
                        modifier = Modifier.Companion.fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.Companion.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Roberto Cuji",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            "2 Qt",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                }
                Surface(
                    modifier = Modifier.Companion.shadow(
                        5.dp, shape = androidx.compose.foundation.shape.RoundedCornerShape(10.dp),
                        ambientColor = DefaultShadowColor.copy(0.2f),
                        spotColor = DefaultShadowColor.copy(0.2f)
                    ).clip(androidx.compose.foundation.shape.RoundedCornerShape(10.dp))
                        .fillMaxWidth()

                ) {
                    Row(
                        modifier = Modifier.Companion.fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.Companion.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Maria Julia",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            "1/2 Qt",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                }
                Surface(
                    modifier = Modifier.Companion.shadow(
                        5.dp, shape = androidx.compose.foundation.shape.RoundedCornerShape(10.dp),
                        ambientColor = DefaultShadowColor.copy(0.2f),
                        spotColor = DefaultShadowColor.copy(0.2f)
                    ).clip(androidx.compose.foundation.shape.RoundedCornerShape(10.dp))
                        .fillMaxWidth()

                ) {
                    Row(
                        modifier = Modifier.Companion.fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.Companion.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "José Rafael",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            "200 $",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                }
            }

        }

    }

}