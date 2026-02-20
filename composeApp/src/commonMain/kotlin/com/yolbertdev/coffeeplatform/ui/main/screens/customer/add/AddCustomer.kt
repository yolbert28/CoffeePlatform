package com.yolbertdev.coffeeplatform.ui.main.screens.customer.add

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.Bitmap
import coil3.compose.AsyncImage
import com.yolbertdev.coffeeplatform.ui.components.PrimaryButton
import com.yolbertdev.coffeeplatform.ui.components.SecondaryTextFieldApp
import com.yolbertdev.coffeeplatform.ui.theme.Gray200

@Composable
expect fun rememberGalleryLauncher(onResult: (ByteArray?) -> Unit): () -> Unit

class AddCustomerScreen() : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow

        val screenModel = getScreenModel<AddCustomerScreenModel>()

        val launchGallery = rememberGalleryLauncher { bytes ->
            if (bytes != null) {
                screenModel.onPhotoCaptured(bytes)
            }
        }

        val uiState by screenModel.uiState.collectAsState()
        val scrollState = rememberScrollState()
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Crear Nuevo Cliente", style = MaterialTheme.typography.titleMedium) },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(Icons.Rounded.ArrowBackIosNew, contentDescription = "Regresar")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                )
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding)
                    .imePadding()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Spacer(Modifier.height(16.dp))

                    // FOTO DE PERFIL CON BADGE DE CÁMARA
                    Box(
                        modifier = Modifier
                            .padding(bottom = 24.dp)
                            .size(140.dp),
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
                            if (uiState.photoBytes != null) {
                                AsyncImage(
                                    model = uiState.photoBytes,
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Icon(
                                    Icons.Rounded.Person,
                                    null,
                                    modifier = Modifier.size(64.dp),
                                    tint = Gray200
                                )
                            }
                        }

                        // Badge flotante de cámara
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
                                Icon(Icons.Default.AddAPhoto, null, modifier = Modifier.size(20.dp))
                            }
                        }
                    }

                    // FORMULARIO ESTILIZADO
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        FormField(
                            label = "Nombre Completo",
                            value = uiState.name,
                            onValueChange = { screenModel.onChangeName(it) },
                            icon = Icons.Rounded.Badge
                        )
                        FormField(
                            label = "Apodo",
                            value = uiState.nickname,
                            onValueChange = { screenModel.onChangeNickname(it) },
                            icon = Icons.Rounded.AlternateEmail
                        )
                        FormField(
                            label = "Número de Cédula",
                            value = uiState.idCard,
                            onValueChange = {
                                val newText = it.filter { char -> char.isDigit() }
                                screenModel.onChangeIdCard(newText)
                            },
                            icon = Icons.Rounded.Fingerprint,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        FormField(
                            label = "Ubicación / Dirección",
                            value = uiState.location,
                            onValueChange = { screenModel.onChangeLocation(it) },
                            icon = Icons.Rounded.LocationOn
                        )
                        FormField(
                            label = "Descripción adicional",
                            value = uiState.description,
                            onValueChange = { screenModel.onChangeDescription(it) },
                            icon = Icons.Rounded.Notes,
                            isLong = true
                        )
                    }

                    Spacer(Modifier.height(32.dp))

                    PrimaryButton(
                        text = "Registrar Cliente",
                        onClick = {
                            screenModel.insertCustomer({
                                navigator.pop()
                            })
                                  },
                        modifier = Modifier.fillMaxWidth().height(56.dp)
                    )

                    Spacer(Modifier.height(24.dp))
                }
            }
        }
    }

}

@Composable
private fun FormField(
    label: String,
    icon: ImageVector,
    isLong: Boolean = false,
    maxLines: Int = 3,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.padding(start = 4.dp, bottom = 6.dp)
        )
        SecondaryTextFieldApp(
            value = value,
            onValueChange = onValueChange,
            leadingIcon = {
                Icon(icon, null)
            },
            singleLine = !isLong,
            minLines = if (isLong) maxLines else 1,
            keyboardOptions = keyboardOptions
        )
    }
}
