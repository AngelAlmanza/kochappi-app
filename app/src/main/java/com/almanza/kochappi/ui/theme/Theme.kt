package com.almanza.kochappi.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = Orange40,
    onPrimary = OnOrange40,
    primaryContainer = OrangeContainer40,
    onPrimaryContainer = OnOrangeContainer40,
    secondary = Slate40,
    onSecondary = OnSlate40,
    secondaryContainer = SlateContainer40,
    onSecondaryContainer = OnSlateContainer40,
    tertiary = Emerald40,
    onTertiary = OnEmerald40,
    tertiaryContainer = EmeraldContainer40,
    onTertiaryContainer = OnEmeraldContainer40,
    error = Red40,
    onError = OnRed40,
    background = Background40,
    onBackground = OnBackground40,
    surface = Surface40,
    onSurface = OnSurface40,
    outline = Outline40,
)

private val DarkColorScheme = darkColorScheme(
    primary = Orange80,
    onPrimary = OnOrange80,
    primaryContainer = OrangeContainer80,
    onPrimaryContainer = OnOrangeContainer80,
    secondary = Slate80,
    onSecondary = OnSlate80,
    secondaryContainer = SlateContainer80,
    onSecondaryContainer = OnSlateContainer80,
    tertiary = Emerald80,
    onTertiary = OnEmerald80,
    tertiaryContainer = EmeraldContainer80,
    onTertiaryContainer = OnEmeraldContainer80,
    error = Red80,
    onError = OnRed80,
    background = Background80,
    onBackground = OnBackground80,
    surface = Surface80,
    onSurface = OnSurface80,
    outline = Outline80,
)

@Composable
fun KochappiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content,
    )
}
