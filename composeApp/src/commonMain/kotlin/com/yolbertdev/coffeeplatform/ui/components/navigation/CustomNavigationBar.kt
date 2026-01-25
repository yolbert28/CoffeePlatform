package com.yolbertdev.coffeeplatform.ui.components.navigation

import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import com.yolbertdev.coffeeplatform.ui.main.screens.customer.CustomerTab
import com.yolbertdev.coffeeplatform.ui.main.screens.loan.LoanTab
import com.yolbertdev.coffeeplatform.ui.main.screens.home.HomeTab
import com.yolbertdev.coffeeplatform.ui.main.screens.payment.PaymentTab
import com.yolbertdev.coffeeplatform.ui.main.screens.ReportTab

@Composable
fun CustomNavigationBar() {
    val tabNavigator = LocalTabNavigator.current
    NavigationBar {
        CustomNavigationBarItem(tabNavigator = tabNavigator, tabModel = HomeTab)
        CustomNavigationBarItem(tabNavigator = tabNavigator, tabModel = CustomerTab)
        CustomNavigationBarItem(tabNavigator = tabNavigator, tabModel = PaymentTab)
        CustomNavigationBarItem(tabNavigator = tabNavigator, tabModel = LoanTab)
        CustomNavigationBarItem(tabNavigator = tabNavigator, tabModel = ReportTab)
    }
}