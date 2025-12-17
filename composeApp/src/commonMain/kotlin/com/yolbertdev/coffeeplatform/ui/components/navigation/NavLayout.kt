package com.yolbertdev.coffeeplatform.ui.components.navigation

import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.window.core.layout.WindowSizeClass
import cafe.adriel.voyager.navigator.tab.TabDisposable
import cafe.adriel.voyager.navigator.tab.TabNavigator

@Composable
fun NavLayout(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val windowSize = currentWindowAdaptiveInfo().windowSizeClass

    val medium = windowSize.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND)

    TabNavigator(
        HomeTab,
        tabDisposable = {
            TabDisposable(
                it,
                listOf(HomeTab, CustomerTab, DebtTab, ReportTab)
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
                modifier = Modifier.padding(innerPadding)
            ) {
                if (medium) {
                    CustomNavigationRail()
                }
                content()
            }
        }
    }
}