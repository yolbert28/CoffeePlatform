package com.yolbertdev.coffeeplatform.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import coffeeplatform.composeapp.generated.resources.Res
import coffeeplatform.composeapp.generated.resources.coffee_logo
import coffeeplatform.composeapp.generated.resources.decorator_bottom
import coffeeplatform.composeapp.generated.resources.decorator_top
import com.yolbertdev.coffeeplatform.ui.components.PrimaryButton
import com.yolbertdev.coffeeplatform.ui.components.SecondaryTextFieldApp
import com.yolbertdev.coffeeplatform.ui.main.MainScreen
import org.jetbrains.compose.resources.painterResource

class LoginScreen : Screen {
    @Composable
    override fun Content() {
        val navigation = LocalNavigator.current
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var rememberMe by remember { mutableStateOf(false) }

        Box(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
        ) {
            // Fondo con los decoradores originales
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(Res.drawable.decorator_top),
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.TopCenter).fillMaxWidth().heightIn(max = 400.dp)
                )
                Image(
                    painter = painterResource(Res.drawable.decorator_bottom),
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().heightIn(max = 400.dp)
                )
            }

            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .widthIn(max = 400.dp)
                    .fillMaxWidth()
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Cabecera manteniendo el Row original pero más estético
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Image(
                        painter = painterResource(Res.drawable.coffee_logo),
                        contentDescription = null,
                        modifier = Modifier.size(40.dp)
                    )
                    Text(
                        text = "Coffee Platform",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                }

                Spacer(Modifier.height(12.dp))

                Text(
                    text = "Tu aplicación financiera de confianza ofreciendo los mejores créditos",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(Modifier.height(32.dp))

                // Bloque de Formulario
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    SecondaryTextFieldApp(
                        value = email,
                        onValueChange = { email = it },
                        placeholder = { Text("Usuario o Correo") }
                    )

                    SecondaryTextFieldApp(
                        value = password,
                        onValueChange = { password = it },
                        placeholder = { Text("Contraseña") }
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = rememberMe,
                            onCheckedChange = { rememberMe = it },
                            colors = CheckboxDefaults.colors(
                                checkedColor = MaterialTheme.colorScheme.primary,
                                uncheckedColor = MaterialTheme.colorScheme.outlineVariant
                            )
                        )
                        Text(
                            text = "Recuérdame",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }

                    PrimaryButton(
                        text = "Iniciar Sesión",
                        onClick = { navigation?.push(MainScreen()) },
                        modifier = Modifier.fillMaxWidth().height(56.dp)
                    )
                }

                Spacer(Modifier.height(32.dp))

                // Footer decorativo y link
                Box(
                    Modifier
                        .height(1.dp)
                        .fillMaxWidth(0.5f)
                        .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                )

                Spacer(Modifier.height(24.dp))

                Row(
                    modifier = Modifier.clickable { /* Acción */ }
                ) {
                    Text(
                        text = "¿Olvidaste tu ",
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                    Text(
                        text = "contraseña?",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}
