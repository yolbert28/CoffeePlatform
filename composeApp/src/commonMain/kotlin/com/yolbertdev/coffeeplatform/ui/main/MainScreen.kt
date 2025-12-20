package com.yolbertdev.coffeeplatform.ui.main

import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.window.core.layout.WindowSizeClass
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.TabDisposable
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.yolbertdev.coffeeplatform.ui.components.navigation.CustomNavigationBar
import com.yolbertdev.coffeeplatform.ui.components.navigation.CustomNavigationRail
import com.yolbertdev.coffeeplatform.ui.main.screens.CustomerTab
import com.yolbertdev.coffeeplatform.ui.main.screens.LoanTab
import com.yolbertdev.coffeeplatform.ui.main.screens.HomeTab
import com.yolbertdev.coffeeplatform.ui.main.screens.PaymentTab
import com.yolbertdev.coffeeplatform.ui.main.screens.ReportTab

class MainScreen : Screen {
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
        ) {
            Scaffold(
                bottomBar = {
                    if (!medium) {
                        CustomNavigationBar()
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