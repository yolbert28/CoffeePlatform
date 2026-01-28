package com.yolbertdev.coffeeplatform.ui.main.screens.customer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import coffeeplatform.composeapp.generated.resources.Res
import coffeeplatform.composeapp.generated.resources.people
import com.yolbertdev.coffeeplatform.domain.model.Customer
import com.yolbertdev.coffeeplatform.ui.components.CustomerListItem
import com.yolbertdev.coffeeplatform.ui.components.FilterSelector
import com.yolbertdev.coffeeplatform.ui.main.screens.customer.add.AddCustomerScreen
import com.yolbertdev.coffeeplatform.ui.components.SearchBarApp
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

        val navigator = LocalNavigator.currentOrThrow
        val screenModel = getScreenModel<CustomerScreenModel>()
        val uiState by screenModel.uiState.collectAsState()

        var searchQuery by remember { mutableStateOf("") }


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

        LaunchedEffect(Unit) {
            screenModel.insertCustomer(customer)
            screenModel.getAllCustomers()
        }

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
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(uiState.customers) {
                    CustomerListItem(customer = it){
                        navigator.parent?.push(AddCustomerScreen())
                    }
                }
            }
        }
    }

}