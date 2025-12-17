package com.yolbertdev.coffeeplatform.ui.components.navigation

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator

@Composable
fun RowScope.CustomNavigationBarItem(tabNavigator: TabNavigator, tabModel: Tab, modifier: Modifier = Modifier) {
    NavigationBarItem(
        selected = tabNavigator.current.key == tabModel.key,
        onClick = {
            tabNavigator.current = tabModel
        },
        label = {
            Text(tabModel.options.title)
        },
        modifier = modifier,
        icon = {
            tabModel.options.icon?.let { painter ->
                Icon(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier.height(20.dp).width(24.dp)
                )
            }
        }
    )

}