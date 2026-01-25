package com.yolbertdev.coffeeplatform.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import coffeeplatform.composeapp.generated.resources.Res
import coffeeplatform.composeapp.generated.resources.roboto_bold
import coffeeplatform.composeapp.generated.resources.roboto_regular
import org.jetbrains.compose.resources.Font

val roboto: FontFamily
    @Composable
    get() = FontFamily(
        Font(
            Res.font.roboto_regular,
            FontWeight.Normal
        ),
        Font(
            Res.font.roboto_bold,
            FontWeight.Bold
        ),
    )

@Composable
fun AppTypography(): Typography {
    val defaultFontFamily = roboto // Usa tu familia de fuentes

    return Typography(
        headlineMedium = TextStyle(
            fontFamily = defaultFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 36.sp
        ),
        headlineSmall = TextStyle(
            fontFamily = defaultFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            color = Green500
        ),
        bodySmall = TextStyle(
            fontFamily = defaultFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = defaultFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp
        ),
        bodyLarge = TextStyle(
            fontFamily = defaultFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        ),
        titleSmall = TextStyle(
            fontFamily = defaultFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
        ),
        titleMedium = TextStyle(
            fontFamily = defaultFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
        ),
        titleLarge = TextStyle(
            fontFamily = defaultFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
        )
    )
}