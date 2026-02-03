package com.yolbertdev.coffeeplatform.ui.main.screens.customer.edit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.yolbertdev.coffeeplatform.domain.model.Customer
import com.yolbertdev.coffeeplatform.ui.components.PrimaryButton
import com.yolbertdev.coffeeplatform.ui.components.TextFieldApp
import kotlinx.coroutines.delay

class EditCustomerScreen(private val customer: Customer) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<EditCustomerScreenModel>()
        val state by viewModel.state.collectAsState()
        val snackbarHostState = remember { SnackbarHostState() }

        // Inicializar datos solo la primera vez
        LaunchedEffect(Unit) {
            viewModel.initCustomer(customer)
        }

        // Manejar éxito
        LaunchedEffect(state.isSaved) {
            if (state.isSaved) {
                snackbarHostState.showSnackbar("Cambios guardados correctamente")
                delay(700)
                navigator.pop() // Volver al detalle
            }
        }

        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                TopAppBar(
                    title = { Text("Editar Perfil") },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Foto (Por ahora editamos la URL/Path como texto,
                // idealmente aquí reutilizarías tu ImagePicker logic)
                TextFieldApp(
                    value = state.photo,
                    onValueChange = { viewModel.onPhotoChange(it) },
                    label = "URL o Path de Foto",
                    imageVector = Icons.Default.Image
                )

                // Nombre
                TextFieldApp(
                    value = state.name,
                    onValueChange = { viewModel.onNameChange(it) },
                    label = "Nombre Completo",
                    imageVector = Icons.Default.Person
                )

                // Apodo
                TextFieldApp(
                    value = state.nickname,
                    onValueChange = { viewModel.onNicknameChange(it) },
                    label = "Apodo / Alias",
                    imageVector = Icons.Default.Face
                )

                // Descripción
                TextFieldApp(
                    value = state.description,
                    onValueChange = { viewModel.onDescriptionChange(it) },
                    label = "Descripción",
                    imageVector = Icons.Default.Description
                )

                // Ubicación
                TextFieldApp(
                    value = state.location,
                    onValueChange = { viewModel.onLocationChange(it) },
                    label = "Ubicación / Dirección",
                    imageVector = Icons.Default.LocationOn
                )

                Spacer(modifier = Modifier.height(24.dp))

                if (state.isLoading) {
                    CircularProgressIndicator()
                } else {
                    PrimaryButton(
                        text = "Guardar Cambios",
                        onClick = { viewModel.saveChanges() }
                    )
                }
            }
        }
    }
}