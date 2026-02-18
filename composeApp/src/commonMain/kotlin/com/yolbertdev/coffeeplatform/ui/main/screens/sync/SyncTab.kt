package com.yolbertdev.coffeeplatform.ui.main.screens.sync


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultShadowColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import coffeeplatform.composeapp.generated.resources.Res
import coffeeplatform.composeapp.generated.resources.download
import org.jetbrains.compose.resources.painterResource

object SyncTab : Tab {

    override val options: TabOptions
        @Composable
        get() {
            val icon = painterResource(Res.drawable.download)
            return remember {
                TabOptions(
                    index = 5u,
                    title = "Sincronizar",
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        val screenModel = getScreenModel<SyncScreenModel>()
        val uiState by screenModel.uiState.collectAsState()

        SyncScreenContent(
            uiState = uiState,
            onExportClick = { screenModel.exportBackup() },
            onImportClick = { screenModel.importBackup() },
            onDismissMessage = { screenModel.clearMessages() }
        )
    }
}

@Composable
fun SyncScreenContent(
    uiState: SyncUiState,
    onExportClick: () -> Unit,
    onImportClick: () -> Unit,
    onDismissMessage: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(32.dp))

        Icon(
            imageVector = Icons.Default.Sync,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(56.dp)
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Sincronización de Datos",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = Color.Black
        )
        Text(
            text = "Exporta o restaura tu base de datos local",
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
        )

        Spacer(Modifier.height(32.dp))

        SyncActionCard(
            title = "Exportar Backup",
            description = "Guarda todos tus clientes, préstamos y pagos en un archivo .json. Úsalo para respaldar tu información o migrarla a otro dispositivo.",
            icon = Icons.Default.CloudUpload,
            iconContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
            iconContentColor = MaterialTheme.colorScheme.primary,
            buttonText = "Exportar datos",
            buttonEnabled = !uiState.isLoading,
            onClick = onExportClick
        )

        Spacer(Modifier.height(16.dp))

        SyncActionCard(
            title = "Restaurar Backup",
            description = "Selecciona un archivo .json generado previamente para restaurar tus datos. Los registros existentes no serán duplicados.",
            icon = Icons.Default.CloudDownload,
            iconContainerColor = Color(0xFF0B626A).copy(alpha = 0.1f),
            iconContentColor = Color(0xFF0B626A),
            buttonText = "Restaurar datos",
            buttonContainerColor = Color(0xFF0B626A),
            buttonEnabled = !uiState.isLoading,
            onClick = onImportClick
        )

        Spacer(Modifier.height(24.dp))

        if (uiState.isLoading) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Procesando...",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
            )
        }

        uiState.successMessage?.let { message ->
            Spacer(Modifier.height(8.dp))
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )
                    TextButton(onClick = onDismissMessage) {
                        Text("OK", color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
            uiState.exportedFilePath?.let { path ->
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "📁 $path",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }

        uiState.errorMessage?.let { error ->
            Spacer(Modifier.height(8.dp))
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f)
                    )
                    TextButton(onClick = onDismissMessage) {
                        Text("OK", color = MaterialTheme.colorScheme.onErrorContainer)
                    }
                }
            }
        }

        Spacer(Modifier.height(32.dp))
        InfoNote()
        Spacer(Modifier.height(32.dp))
    }
}

@Composable
private fun SyncActionCard(
    title: String,
    description: String,
    icon: ImageVector,
    iconContainerColor: Color,
    iconContentColor: Color,
    buttonText: String,
    buttonEnabled: Boolean,
    buttonContainerColor: Color = MaterialTheme.colorScheme.primary,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(3.dp, shape = RoundedCornerShape(16.dp), ambientColor = DefaultShadowColor.copy(0.1f)),
        color = Color.White,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(iconContainerColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconContentColor,
                        modifier = Modifier.size(26.dp)
                    )
                }
                Spacer(Modifier.width(12.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color.Black
                )
            }

            Spacer(Modifier.height(12.dp))

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = onClick,
                enabled = buttonEnabled,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = buttonContainerColor)
            ) {
                Text(
                    text = buttonText,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
private fun InfoNote() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFFF1F5F4),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "ℹ️ ¿Cómo funciona?",
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                color = Color.Black
            )
            Spacer(Modifier.height(8.dp))
            NoteItem("El archivo .json generado contiene todos tus clientes, préstamos y pagos.")
            NoteItem("Puedes transferirlo a otro dispositivo o guardarlo en la nube manualmente.")
            NoteItem("Al restaurar, los datos existentes no se duplicarán.")
            NoteItem("Compatible con Android y escritorio (JVM).")
        }
    }
}

@Composable
private fun NoteItem(text: String) {
    Row(modifier = Modifier.padding(vertical = 2.dp)) {
        Text("• ", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
        Text(text, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
    }
}