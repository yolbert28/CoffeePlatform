package com.yolbertdev.coffeeplatform.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

//private val DarkColorScheme = darkColorScheme(
//    background = Color.White,
//    primary = Green500,
//    onPrimary = Color.White,
//    outline = Gray700,
//    outlineVariant = Gray500
//)

private val LightColorScheme = lightColorScheme(
    primary = Green500,
    onPrimary = Color.White,

    secondary = Color(0xFF0B626A),
    secondaryContainer = Green500,
    onSecondaryContainer = Color.White,

    outline = Gray700,
    outlineVariant = Gray500,
    background = Gray900,
    onBackground = Color.Black,

    surface = Color.White,
    onSurface = Green500,
    surfaceContainer = Color.White,
    onSurfaceVariant = Gray200



    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun CoffeePlatformTheme(
//    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme
//        when {
//        darkTheme -> DarkColorScheme
//        else -> LightColorScheme
//    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography(),
        content = content
    )
}