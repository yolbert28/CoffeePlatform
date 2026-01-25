package com.yolbertdev.coffeeplatform.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
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
import com.yolbertdev.coffeeplatform.ui.components.TextFieldApp
import com.yolbertdev.coffeeplatform.ui.main.MainScreen
import org.jetbrains.compose.resources.painterResource

class LoginScreen : Screen {
    @Composable
    override fun Content() {
        val navigation = LocalNavigator.current
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(Res.drawable.decorator_top),
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                    modifier = Modifier.align(
                        Alignment.TopCenter
                    ).fillMaxWidth().heightIn(max = 400.dp)
                )
                Image(
                    painter = painterResource(Res.drawable.decorator_bottom),
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                    modifier = Modifier.align(
                        Alignment.BottomCenter
                    ).fillMaxWidth().heightIn(max = 400.dp)
                )
            }
            Column(
                modifier = Modifier.padding(horizontal = 20.dp).widthIn(max = 400.dp).fillMaxWidth()
                    .align(Alignment.Center),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Image(
                        painter = painterResource(Res.drawable.coffee_logo),
                        contentDescription = null
                    )
                    Text("Coffee Platform", style = MaterialTheme.typography.headlineSmall)
                }
                Text(
                    "Tu aplicación financiera de confianza ofreciendo los mejores creditos",
                    style = TextStyle(fontWeight = FontWeight.Bold, textAlign = TextAlign.Center),
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
                Spacer(Modifier.height(10.dp))
                TextFieldApp(
                    value = "",
                    onValueChange = {}
                )
                Column(Modifier.fillMaxWidth()) {
                    TextFieldApp(
                        value = "",
                        onValueChange = {}
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Checkbox(
                            checked = false,
                            onCheckedChange = {},
                            colors = CheckboxDefaults.colors(
                                checkedColor = MaterialTheme.colorScheme.outline,
                                uncheckedColor = MaterialTheme.colorScheme.outline,
                            )
                        )
                        Text(
                            "Recuerdame",
                            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.outlineVariant)
                        )
                    }
                    PrimaryButton(text = "Iniciar Sesión", onClick = {navigation?.push(MainScreen())})
                }
                Box(
                    Modifier.height(2.dp).fillMaxWidth()
                        .padding(horizontal = 20.dp).background(MaterialTheme.colorScheme.outline)
                )
                Row {
                    Text(
                        "¿Olvidaste tu ",
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.outlineVariant)
                    )
                    Text(
                        "contraseña?",
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.primary)
                    )
                }
            }
        }
    }
}