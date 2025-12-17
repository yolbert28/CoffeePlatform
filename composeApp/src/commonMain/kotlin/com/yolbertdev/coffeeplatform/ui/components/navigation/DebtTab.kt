package com.yolbertdev.coffeeplatform.ui.components.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import coffeeplatform.composeapp.generated.resources.Res
import coffeeplatform.composeapp.generated.resources.dollar
import coffeeplatform.composeapp.generated.resources.people
import org.jetbrains.compose.resources.painterResource

object DebtTab : Tab{
    override val options: TabOptions
        @Composable
        get(){
            val icon = painterResource(Res.drawable.dollar)
            return remember {
                TabOptions(
                    index = 0u,
                    title = "Deudas",
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        TODO("Not yet implemented")
    }

}