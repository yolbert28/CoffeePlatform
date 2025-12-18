package com.yolbertdev.coffeeplatform.ui.main.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import coffeeplatform.composeapp.generated.resources.Res
import coffeeplatform.composeapp.generated.resources.people
import org.jetbrains.compose.resources.painterResource

object CustomerTab : Tab{
    override val options: TabOptions
        @Composable
        get(){
            val icon = painterResource(Res.drawable.people)
            return remember {
                TabOptions(
                    index = 0u,
                    title = "Clientes",
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        Text("Customer")
    }

}