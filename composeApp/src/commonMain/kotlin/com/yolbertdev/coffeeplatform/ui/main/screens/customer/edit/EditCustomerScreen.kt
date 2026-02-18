package com.yolbertdev.coffeeplatform.ui.main.screens.customer.edit

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.AsyncImage
import com.yolbertdev.coffeeplatform.domain.model.Customer
import com.yolbertdev.coffeeplatform.ui.components.FormField
import com.yolbertdev.coffeeplatform.ui.components.PrimaryButton
import com.yolbertdev.coffeeplatform.ui.main.screens.customer.add.rememberGalleryLauncher

data class EditCustomerScreen(val customer: Customer) : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinScreenModel<EditCustomerScreenModel>()
        val state by viewModel.state.collectAsState()

        LaunchedEffect(Unit) {
            viewModel.init(customer)
        }

        LaunchedEffect(state.isSaved) {
            if (state.isSaved) {
                navigator.pop()
            }
        }

        val launchGallery = rememberGalleryLauncher { bytes ->
            if (bytes != null) {
                viewModel.onPhotoSelected(bytes)
            }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Editar Perfil", style = MaterialTheme.typography.titleMedium) },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(Icons.Rounded.ArrowBackIosNew, "Atrás")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(16.dp))

                // FOTO DE PERFIL CON BADGE DE EDICIÓN
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .padding(bottom = 24.dp),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .border(
                                2.dp,
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                                CircleShape
                            )
                            .clickable { launchGallery() },
                        contentAlignment = Alignment.Center
                    ) {
                        if (state.photo.isNotEmpty()) {
                            AsyncImage(
                                model = state.photo,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Icon(
                                Icons.Default.Person,
                                null,
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colorScheme.outline
                            )
                        }
                    }

                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White,
                        modifier = Modifier.size(40.dp),
                        shadowElevation = 4.dp
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.clickable { launchGallery() }) {
                            Icon(Icons.Default.Edit, null, modifier = Modifier.size(20.dp))
                        }
                    }
                }

                // FORMULARIO CON FORMFIELLD
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    FormField(
                        label = "Nombre Completo",
                        value = state.name,
                        onValueChange = viewModel::onNameChange,
                        icon = Icons.Default.Badge
                    )
                    FormField(
                        label = "Apodo",
                        value = state.nickname,
                        onValueChange = viewModel::onNicknameChange,
                        icon = Icons.Default.AlternateEmail
                    )
                    FormField(
                        label = "Ubicación / Dirección",
                        value = state.location,
                        onValueChange = viewModel::onLocationChange,
                        icon = Icons.Default.LocationOn
                    )
                    FormField(
                        label = "Descripción adicional",
                        value = state.description,
                        onValueChange = viewModel::onDescriptionChange,
                        icon = Icons.Default.Notes,
                        isLong = true
                    )
                }

                Spacer(Modifier.height(32.dp))

                if (state.isLoading) {
                    CircularProgressIndicator()
                } else {
                    PrimaryButton(
                        text = "Guardar Cambios",
                        onClick = {
                            viewModel.save() },
                        modifier = Modifier.fillMaxWidth().height(56.dp)
                    )
                }
                
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}