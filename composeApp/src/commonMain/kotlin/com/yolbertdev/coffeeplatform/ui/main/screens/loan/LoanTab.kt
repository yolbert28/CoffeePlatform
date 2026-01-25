package com.yolbertdev.coffeeplatform.ui.main.screens.loan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import coffeeplatform.composeapp.generated.resources.Res
import coffeeplatform.composeapp.generated.resources.dollar
import com.yolbertdev.coffeeplatform.domain.model.Customer
import com.yolbertdev.coffeeplatform.domain.model.Loan
import com.yolbertdev.coffeeplatform.ui.components.FilterSelector
import com.yolbertdev.coffeeplatform.ui.components.LoanItem
import com.yolbertdev.coffeeplatform.ui.components.SearchBarApp
import com.yolbertdev.coffeeplatform.ui.main.screens.loan.detail.LoanDetailScreen
import org.jetbrains.compose.resources.painterResource

object LoanTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
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

        val navigator = LocalNavigator.currentOrThrow

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

            // Reducida la separación entre búsqueda y filtro
            Spacer(Modifier.height(4.dp))

            FilterSelector()

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(10) {
                    LoanItem(loan, onClick = {
                        navigator.parent?.push(LoanDetailScreen(loan, customer))
                    })
                }
                item {
                    Spacer(Modifier.height(16.dp))
                }
            }
        }
    }
}