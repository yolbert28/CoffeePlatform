package com.yolbertdev.coffeeplatform.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.Bitmap
import coil3.compose.AsyncImage
import com.yolbertdev.coffeeplatform.ui.theme.Gray200

@Composable
expect fun rememberCameraLauncher(onResult: (Bitmap?) -> Unit): () -> Unit

@Composable
fun CommonModalAddCustomer(onDismiss: () -> Unit) {
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    val launchCamera = rememberCameraLauncher { bitmap = it }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = "Crear Nuevo Cliente",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // FOTO DE PERFIL CON BADGE DE CÁMARA
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
                        .border(2.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f), CircleShape)
                        .clickable { launchCamera() },
                    contentAlignment = Alignment.Center
                ) {
                    if (bitmap != null) {
                        AsyncImage(
                            model = bitmap,
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
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.clickable { launchCamera() }) {
                        Icon(Icons.Default.AddAPhoto, null, modifier = Modifier.size(20.dp))
                    }
                }
            }

            // FORMULARIO ESTILIZADO
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                FormField(label = "Nombre Completo", icon = Icons.Rounded.Badge)
                FormField(label = "Apodo", icon = Icons.Rounded.AlternateEmail)
                FormField(label = "Número de Cédula", icon = Icons.Rounded.Fingerprint)
                FormField(label = "Ubicación / Dirección", icon = Icons.Rounded.LocationOn)
                FormField(label = "Descripción adicional", icon = Icons.Rounded.Notes, isLong = true)
            }

            Spacer(Modifier.height(32.dp))

            PrimaryButton(
                text = "Registrar Cliente",
                onClick = { /* Lógica de guardado */ },
                modifier = Modifier.fillMaxWidth().height(56.dp)
            )

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun FormField(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isLong: Boolean = false,
    maxLines: Int = 3
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
            value = "",
            onValueChange = {},
            leadingIcon = {
                Icon(icon, null)
            },
            singleLine = !isLong,
            minLines = if (isLong) maxLines else 1
        )
    }
}

@Composable
expect fun ModalAddCustomer(onDismiss: () -> Unit)