package com.saimetal.furniture.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkScheme = darkColorScheme(
    primary = Copper,
    secondary = Sand,
    tertiary = Olive,
    background = Iron,
    surface = Steel
)

private val LightScheme = lightColorScheme(
    primary = Wood,
    secondary = Copper,
    tertiary = Olive,
    background = Mist,
    surface = ColorWhite
)

@Composable
fun SaiMetalTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkScheme else LightScheme,
        typography = AppTypography,
        content = content
    )
}
