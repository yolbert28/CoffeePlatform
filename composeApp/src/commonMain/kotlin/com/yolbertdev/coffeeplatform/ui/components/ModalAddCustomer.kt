package com.yolbertdev.coffeeplatform.ui.components

import androidx.compose.animation.core.copy
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.Bitmap
import coil3.compose.AsyncImage

@Composable
expect fun rememberCameraLauncher(onResult: (Bitmap?) -> Unit): () -> Unit
@Composable
fun CommonModalAddCustomer(onDismiss: () -> Unit) {
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    val launchCamera = rememberCameraLauncher { bitmap = it }

    LazyColumn(
        modifier = Modifier.padding(horizontal = 20.dp)
    ) {
        item {
            Text(text = "Nuevo cliente", modifier = Modifier.fillMaxWidth(), style = MaterialTheme.typography.titleLarge.copy(color = Color.Black, textAlign = TextAlign.Center))
            Spacer(Modifier.height(16.dp))
            // BOX DE LA FOTO 200.dp
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.outlineVariant)
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
                        Icon(Icons.Default.AddAPhoto, null, modifier = Modifier.size(40.dp))
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            Text(text = "Cédula:", modifier = Modifier.padding(start = 10.dp), style = MaterialTheme.typography.bodyLarge)
            SecondaryTextFieldApp(
                value = "",
                onValueChange = {},
            )
            Spacer(Modifier.height(16.dp))
            Text("Nombre")
            SecondaryTextFieldApp(
                value = "",
                onValueChange = {},
            )
            Spacer(Modifier.height(16.dp))
            Text("Apodo")
            SecondaryTextFieldApp(
                value = "",
                onValueChange = {},
            )
            Spacer(Modifier.height(16.dp))
            Text("Descripción:")
            SecondaryTextFieldApp(
                value = "",
                onValueChange = {},
            )
            Spacer(Modifier.height(16.dp))
            Text("Dirección")
            SecondaryTextFieldApp(
                value = "",
                onValueChange = {},
            )
            Spacer(Modifier.height(32.dp))
            PrimaryButton(
                text = "Guardar",
                onClick = {},
                modifier = Modifier
            )
            Spacer(Modifier.height(16.dp))
        }

    }
}

@Composable
expect fun ModalAddCustomer()