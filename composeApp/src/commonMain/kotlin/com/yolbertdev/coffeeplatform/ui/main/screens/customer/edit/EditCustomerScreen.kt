package com.yolbertdev.coffeeplatform.ui.main.screens.customer.edit

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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.AsyncImage
import com.yolbertdev.coffeeplatform.domain.model.Customer
import com.yolbertdev.coffeeplatform.ui.components.PrimaryButton
import com.yolbertdev.coffeeplatform.ui.components.TextFieldApp
// Importamos el launcher común que creamos en el paso anterior
import com.yolbertdev.coffeeplatform.ui.main.screens.customer.add.rememberGalleryLauncher
import java.io.File

data class EditCustomerScreen(val customer: Customer) : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<EditCustomerScreenModel>()
        val state by viewModel.state.collectAsState()

        // Inicialización de datos
        LaunchedEffect(Unit) {
            viewModel.init(customer)
        }

        // Navegación al guardar
        LaunchedEffect(state.isSaved) {
            if (state.isSaved) {
                navigator.pop()
            }
        }

        // --- CORRECCIÓN AQUÍ ---
        // Usamos el launcher común en lugar del específico de Android
        val launchGallery = rememberGalleryLauncher { bytes ->
            if (bytes != null) {
                viewModel.onPhotoSelected(bytes)
            }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Editar Cliente") },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(Icons.Rounded.ArrowBackIosNew, "Atrás")
                        }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Foto Circular Clickeable
                Box(contentAlignment = Alignment.BottomEnd) {
                    // Nota: Si state.photo es una ruta local, File(path) funciona bien.
                    val model = if (state.photo.isNotEmpty()) File(state.photo) else null

                    AsyncImage(
                        model = model,
                        contentDescription = "Foto",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .clickable { launchGallery() }, // Abrir galería al tocar imagen
                        // Usamos un vector painter para el placeholder (compatible common)
                        placeholder = androidx.compose.ui.graphics.vector.rememberVectorPainter(Icons.Default.Person),
                        error = androidx.compose.ui.graphics.vector.rememberVectorPainter(Icons.Default.Person)
                    )

                    // Botón pequeño flotante
                    SmallFloatingActionButton(
                        onClick = { launchGallery() }, // Abrir galería al tocar botón
                        modifier = Modifier.offset(4.dp, 4.dp)
                    ) {
                        Icon(Icons.Default.Edit, null)
                    }
                }

                TextFieldApp(value = state.name, onValueChange = viewModel::onNameChange, label = "Nombre", imageVector = Icons.Default.Person)
                TextFieldApp(value = state.nickname, onValueChange = viewModel::onNicknameChange, label = "Apodo", imageVector = Icons.Default.Face)
                TextFieldApp(value = state.description, onValueChange = viewModel::onDescriptionChange, label = "Descripción", imageVector = Icons.Default.Description)
                TextFieldApp(value = state.location, onValueChange = viewModel::onLocationChange, label = "Ubicación", imageVector = Icons.Default.LocationOn)

                Spacer(Modifier.height(16.dp))

                if (state.isLoading) {
                    CircularProgressIndicator()
                } else {
                    PrimaryButton(text = "Guardar Cambios", onClick = viewModel::save)
                }
            }
        }
    }
}