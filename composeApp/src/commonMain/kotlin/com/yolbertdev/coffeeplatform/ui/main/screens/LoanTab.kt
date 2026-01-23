package com.yolbertdev.coffeeplatform.ui.main.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.DefaultShadowColor
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import coffeeplatform.composeapp.generated.resources.Res
import coffeeplatform.composeapp.generated.resources.dollar
import coffeeplatform.composeapp.generated.resources.search
import com.yolbertdev.coffeeplatform.domain.model.Loan
import com.yolbertdev.coffeeplatform.ui.components.CustomerListItem
import com.yolbertdev.coffeeplatform.ui.components.FilterSelector
import com.yolbertdev.coffeeplatform.ui.components.LoanItem
import com.yolbertdev.coffeeplatform.ui.components.TextFieldApp
import org.jetbrains.compose.resources.painterResource

object LoanTab : Tab{
    override val options: TabOptions
        @Composable
        get(){
            val icon = painterResource(Res.drawable.dollar)
            return remember {
                TabOptions(
                    index = 3u,
                    title = "Prestamos",
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        val loan = Loan(
            id = 1,
            customerId = 1,
            interestRate = 0.125,
            description = "Prestamo al Coño e madre de Luis para ver si se anima a estudiar y deja de darselo al profesor en el Cabiguan",
            paymentDate = "12/03/2024",
            paymentType = "Dolares",
            quantity = 1000.0,
            paid = 0.0,
            statusId = 1,
            creationDate = "12/12/2023",
            updateDate = "12/10/2023"
        )

        Column(
            Modifier.padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(24.dp))
            Text("Prestamos", style = MaterialTheme.typography.titleLarge)
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
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(10){
                    LoanItem(loan)
                }
            }
        }
    }

}