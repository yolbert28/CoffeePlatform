package com.yolbertdev.coffeeplatform.ui.main

import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.TabDisposable
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.yolbertdev.coffeeplatform.ui.components.navigation.CustomNavigationBar
import com.yolbertdev.coffeeplatform.ui.components.navigation.CustomNavigationRail
import com.yolbertdev.coffeeplatform.ui.main.screens.customer.CustomerTab
import com.yolbertdev.coffeeplatform.ui.main.screens.loan.LoanTab
import com.yolbertdev.coffeeplatform.ui.main.screens.home.HomeTab
import com.yolbertdev.coffeeplatform.ui.main.screens.payment.PaymentTab
import com.yolbertdev.coffeeplatform.ui.main.screens.ReportTab
import okhttp3.internal.wait

class MainScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val windowSize = currentWindowAdaptiveInfo().windowSizeClass

        val medium = windowSize.isWidthAtLeastBreakpoint(WindowSizeClass.Companion.WIDTH_DP_EXPANDED_LOWER_BOUND)

        TabNavigator(
            HomeTab,
            tabDisposable = {
                TabDisposable(
                    it,
                    listOf(HomeTab, CustomerTab, PaymentTab, LoanTab, ReportTab)
                )
            }
        ) { tabNavigator ->

            // Determinar el título basado en la sección actual
            val sectionTitle = when (tabNavigator.current) {
                is CustomerTab -> "Gestión de Clientes"
                is PaymentTab -> "Gestión de Pagos"
                is LoanTab -> "Gestión de Préstamos"
                is ReportTab -> "Reportes"
                else -> null // Home no lleva TopAppBar
            }

            Scaffold(
                topBar = {
                    if (sectionTitle != null) {
                        TopAppBar(
                            title = {
                                Text(
                                    text = sectionTitle,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = Color.Black,
                                    modifier = Modifier.padding(top = 16.dp, start = 4.dp)
                                )
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.background
                            )
                        )
                    }
                },
                bottomBar = {
                    if (!medium) {
                        CustomNavigationBar()
                    }
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {},
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White
                    ){
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null
                        )
                    }
                }
            ) { innerPadding ->

                FlowRow(
                    modifier = Modifier.Companion.padding(innerPadding)
                ) {
                    if (medium) {
                        CustomNavigationRail()
                    }
                    CurrentTab()
                }
            }
        }
    }

}