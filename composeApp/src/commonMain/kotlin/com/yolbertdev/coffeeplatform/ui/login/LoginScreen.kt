package com.yolbertdev.coffeeplatform.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import coffeeplatform.composeapp.generated.resources.Res
import coffeeplatform.composeapp.generated.resources.decorator_bottom
import coffeeplatform.composeapp.generated.resources.decorator_corner
import com.yolbertdev.coffeeplatform.ui.components.TextFieldApp
import org.jetbrains.compose.resources.painterResource

class LoginScreen : Screen {
    @Composable
    override fun Content() {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Row(
                    Modifier.align(
                        Alignment.TopStart
                    ).fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(Res.drawable.decorator_corner),
                        contentScale = ContentScale.Fit,
                        contentDescription = null,
                        modifier = Modifier.weight(1f)
                    )
                    Box(Modifier.weight(1f))
                }
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
                modifier = Modifier.fillMaxWidth().align(Alignment.Center),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Coffee Platform", style = MaterialTheme.typography.headlineLarge)
                Text(
                    "Tu aplicación financiera de confianza ofreciendo los mejores creditos",
                    style = TextStyle(fontWeight = FontWeight.Bold, textAlign = TextAlign.Center),
                    modifier = Modifier.widthIn(max = 400.dp).padding(horizontal = 12.dp)
                )
                TextFieldApp(
                    value = "",
                    onValueChange = {}
                )
                TextFieldApp(
                    value = "",
                    onValueChange = {}
                )
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp).widthIn(max = 400.dp).fillMaxWidth(),
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
                    Text("Recuerdame")
                }
                Button(
                    onClick = {},
                    modifier = Modifier.padding(horizontal = 10.dp).widthIn(max = 400.dp).fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Ingresar")
                }
                Box(
                    Modifier.height(2.dp).widthIn(max = 400.dp).fillMaxWidth()
                        .padding(horizontal = 8.dp).background(MaterialTheme.colorScheme.outline)
                )
                Text("¿Olvidaste tu contraseña?")
            }
        }
    }
}