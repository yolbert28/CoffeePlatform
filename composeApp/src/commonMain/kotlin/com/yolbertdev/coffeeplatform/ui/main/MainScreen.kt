package com.yolbertdev.coffeeplatform.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.TabDisposable
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.yolbertdev.coffeeplatform.getPlatform
import com.yolbertdev.coffeeplatform.ui.components.navigation.CustomNavigationBar
import com.yolbertdev.coffeeplatform.ui.components.navigation.CustomNavigationRail
import com.yolbertdev.coffeeplatform.ui.main.screens.ReportTab
import com.yolbertdev.coffeeplatform.ui.main.screens.customer.CustomerTab
import com.yolbertdev.coffeeplatform.ui.main.screens.home.HomeTab
import com.yolbertdev.coffeeplatform.ui.main.screens.loan.LoanTab
import com.yolbertdev.coffeeplatform.ui.main.screens.payment.PaymentTab

class MainScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val windowSize = currentWindowAdaptiveInfo().windowSizeClass
        val medium = windowSize.isWidthAtLeastBreakpoint(WindowSizeClass.Companion.WIDTH_DP_EXPANDED_LOWER_BOUND)
        val isDesktop = getPlatform().name.contains("Java", ignoreCase = true) || getPlatform().name.contains("Desktop", ignoreCase = true)

        TabNavigator(
            HomeTab,
            tabDisposable = {
                TabDisposable(
                    it,
                    listOf(HomeTab, CustomerTab, PaymentTab, LoanTab, ReportTab)
                )
            }
        ) { tabNavigator ->

            val sectionTitle = when (tabNavigator.current) {
                is CustomerTab -> "Gestión de Clientes"
                is PaymentTab -> "Gestión de Pagos"
                is LoanTab -> "Gestión de Préstamos"
                is ReportTab -> "Reportes"
                else -> null
            }

            Scaffold(
                topBar = {
                    // Solo mostramos la TopBar si hay un título Y NO es Desktop
                    if (sectionTitle != null && !isDesktop) {
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
                }
            ) { innerPadding ->
                Row(
                    modifier = Modifier.padding(innerPadding)
                ) {
                    if (medium) {
                        CustomNavigationRail()
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        CurrentTab()
                    }
                }
            }
        }
    }
}