package com.yolbertdev.coffeeplatform.ui.components.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import coffeeplatform.composeapp.generated.resources.Res
import coffeeplatform.composeapp.generated.resources.coffee_logo
import com.yolbertdev.coffeeplatform.ui.main.screens.CustomerTab
import com.yolbertdev.coffeeplatform.ui.main.screens.DebtTab
import com.yolbertdev.coffeeplatform.ui.main.screens.HomeTab
import com.yolbertdev.coffeeplatform.ui.main.screens.PaymentTab
import com.yolbertdev.coffeeplatform.ui.main.screens.ReportTab
import org.jetbrains.compose.resources.painterResource

@Composable
fun CustomNavigationRail() {
    NavigationRail(
        modifier = Modifier.widthIn(max = 300.dp),
        header = {
            Row(
                modifier = Modifier.padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Image(
                    painter = painterResource(Res.drawable.coffee_logo),
                    contentDescription = null
                )
                Text("Coffee Platform", style = MaterialTheme.typography.headlineLarge.copy(fontSize = 28.sp))
            }
        }
    ) {
        val tabNavigator = LocalTabNavigator.current
        CustomNavigationRailItem(tabNavigator = tabNavigator, tabModel = HomeTab)
        CustomNavigationRailItem(tabNavigator = tabNavigator, tabModel = CustomerTab)
        CustomNavigationRailItem(tabNavigator = tabNavigator, tabModel = PaymentTab)
        CustomNavigationRailItem(tabNavigator = tabNavigator, tabModel = DebtTab)
        CustomNavigationRailItem(tabNavigator = tabNavigator, tabModel = ReportTab)
    }
}