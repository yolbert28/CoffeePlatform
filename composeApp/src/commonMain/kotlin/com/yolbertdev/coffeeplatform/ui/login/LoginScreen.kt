package com.yolbertdev.coffeeplatform.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coffeeplatform.composeapp.generated.resources.Res
import coffeeplatform.composeapp.generated.resources.coffee_logo
import coffeeplatform.composeapp.generated.resources.decorator_bottom
import coffeeplatform.composeapp.generated.resources.decorator_top
import com.yolbertdev.coffeeplatform.ui.components.PrimaryButton
import com.yolbertdev.coffeeplatform.ui.components.SecondaryTextFieldApp
import com.yolbertdev.coffeeplatform.ui.main.MainScreen
import com.yolbertdev.coffeeplatform.ui.main.screens.register.RegisterScreen
import org.jetbrains.compose.resources.painterResource

class LoginScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<LoginScreenModel>()
        val state by viewModel.state.collectAsState()

        var rememberMe by remember { mutableStateOf(false) }
        var passwordVisible by remember { mutableStateOf(false) }
        // Efecto para navegar al Home si el login es exitoso
        LaunchedEffect(state.isLoggedIn) {
            if (state.isLoggedIn) {
                navigator.replaceAll(MainScreen())
            }
        }

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
                    .align(Alignment.Center)
                    .verticalScroll(rememberScrollState()), // Scroll por seguridad en pantallas pequeñas
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
                    // Campo Usuario
                    SecondaryTextFieldApp(
                        value = state.username,
                        onValueChange = { viewModel.onUsernameChange(it) },
                        placeholder = { Text("Usuario o Correo") }
                    )

                    // Campo Contraseña
                    SecondaryTextFieldApp(
                        value = state.password,
                        onValueChange = { viewModel.onPasswordChange(it) },
                        placeholder = { Text("Contraseña") },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        trailingIcon = {
                            val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                            val description = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"

                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(imageVector = image, contentDescription = description)
                            }
                        }
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

                    if (state.error != null) {
                        Text(
                            text = state.error!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }

                    if (state.isLoading) {
                        Box(modifier = Modifier.fillMaxWidth().height(56.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    } else {
                        PrimaryButton(
                            text = "Iniciar Sesión",
                            onClick = { viewModel.login() },
                            modifier = Modifier.fillMaxWidth().height(56.dp)
                        )
                    }
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

                // Link de Registro (NUEVO, manteniendo estilo)
                Row(
                    modifier = Modifier.clickable { navigator.push(RegisterScreen()) }
                ) {
                    Text(
                        text = "¿No tienes cuenta? ",
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                    Text(
                        text = "Regístrate",
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