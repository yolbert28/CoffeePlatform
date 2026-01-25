package com.yolbertdev.coffeeplatform.ui.main.screens.payment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import coffeeplatform.composeapp.generated.resources.Res
import coffeeplatform.composeapp.generated.resources.payment
import com.yolbertdev.coffeeplatform.domain.model.Customer
import com.yolbertdev.coffeeplatform.ui.components.FilterSelector
import com.yolbertdev.coffeeplatform.ui.components.MainPaymentItem
import com.yolbertdev.coffeeplatform.ui.components.SearchBarApp
import com.yolbertdev.coffeeplatform.ui.main.screens.payment.detail.PaymentDetailScreen
import org.jetbrains.compose.resources.painterResource

object PaymentTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val icon = painterResource(Res.drawable.payment)
            return remember {
                TabOptions(
                    index = 2u,
                    title = "Pagos",
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {

        val customer = Customer(
            id = 1,
            idCard = "123456789",
            name = "David big",
            nickname = "JDoe",
            description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
            creditLevel = 3,
            location = "",
            photo = "https://i.pinimg.com/736x/3f/20/7c/3f207cedc0a28a24ce344483bfe91b8c.jpg",
            creationDate = 0,
            updateDate = 0,
            statusId = 1L
        )

        val navigator = LocalNavigator.currentOrThrow

        var searchQuery by remember { mutableStateOf("") }

        Column(
            Modifier.padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(12.dp))

            // Barra de búsqueda reutilizable
            SearchBarApp(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = "Buscar por cliente o descripción..."
            )

            Spacer(Modifier.height(4.dp))
            FilterSelector()
            LazyColumn(
                modifier = Modifier.padding(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(5) {
                    MainPaymentItem(
                        customerNickname = "Roberto",
                        customerName = "Roberto Cuji",
                        customerPhoto = "https://plus.unsplash.com/premium_photo-1689568126014-06fea9d5d341?fm=jpg&q=60&w=3000&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MXx8cHJvZmlsZXxlbnwwfHwwfHx8MA%3D%3D",
                        amount = "2 Qt",
                        date = "Hoy, 10:30 AM",
                        onClick = {
                            navigator.parent?.push(PaymentDetailScreen(
                                amount = "2 Qt",
                                paymentType = "Efectivo",
                                date = "Hoy",
                                customer = customer
                            ))
                        }
                    )
                }
            }
        }
    }

}