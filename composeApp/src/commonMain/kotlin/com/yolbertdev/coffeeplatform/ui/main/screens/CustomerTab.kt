package com.yolbertdev.coffeeplatform.ui.main.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridScope
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultShadowColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import coffeeplatform.composeapp.generated.resources.Res
import coffeeplatform.composeapp.generated.resources.home
import coffeeplatform.composeapp.generated.resources.notification
import coffeeplatform.composeapp.generated.resources.people
import coffeeplatform.composeapp.generated.resources.search
import com.yolbertdev.coffeeplatform.ui.components.CustomerListItem
import com.yolbertdev.coffeeplatform.ui.components.FilterSelector
import com.yolbertdev.coffeeplatform.ui.components.TextFieldApp
import com.yolbertdev.coffeeplatform.ui.theme.Gray200
import com.yolbertdev.coffeeplatform.ui.theme.Gray700
import com.yolbertdev.coffeeplatform.ui.theme.Gray900
import org.jetbrains.compose.resources.painterResource

object CustomerTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val icon = painterResource(Res.drawable.people)
            return remember {
                TabOptions(
                    index = 1u,
                    title = "Clientes",
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        Column(
            Modifier.padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(24.dp))
            Text("Clientes", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.surface).padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TextFieldApp(
                    modifier = Modifier.weight(5f),
                    value = "",
                    onValueChange = {}
                )
                IconButton(
                    modifier = Modifier.shadow(
                        5.dp, shape = RoundedCornerShape(10.dp),
                        ambientColor = DefaultShadowColor.copy(0.2f),
                        spotColor = DefaultShadowColor.copy(0.2f)
                    ).weight(1f).aspectRatio(1f),
                    onClick = {},
                    enabled = true,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.search),
                        contentDescription = null,
                        modifier = Modifier.padding(4.dp).size(28.dp)
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            FilterSelector()
            LazyColumn(
                modifier = Modifier.padding(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(20) {
                    CustomerListItem()
                }
            }
        }
    }

}