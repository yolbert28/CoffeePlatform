package com.yolbertdev.coffeeplatform.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import coffeeplatform.composeapp.generated.resources.Res
import coffeeplatform.composeapp.generated.resources.decorator_bottom
import org.jetbrains.compose.resources.painterResource

class HomeScreen : Screen {
    @Composable
    override fun Content() {
        Box(){
            Image(painterResource(Res.drawable.decorator_bottom) , null)
        }
    }
}