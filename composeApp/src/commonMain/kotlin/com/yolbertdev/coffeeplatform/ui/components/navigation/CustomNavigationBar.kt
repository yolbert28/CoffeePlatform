package com.yolbertdev.coffeeplatform.ui.components.navigation

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator

@Composable
fun CustomNavigationBar() {
    val tabNavigator = LocalTabNavigator.current
    NavigationBar {
        CustomNavigationBarItem(tabNavigator = tabNavigator, tabModel = HomeTab)
        CustomNavigationBarItem(tabNavigator = tabNavigator, tabModel = CustomerTab)
        CustomNavigationBarItem(tabNavigator = tabNavigator, tabModel = DebtTab)
        CustomNavigationBarItem(tabNavigator = tabNavigator, tabModel = ReportTab)
    }
}