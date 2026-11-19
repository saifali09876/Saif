package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = ForestTeal,
    onPrimary = Color.White,
    primaryContainer = ForestTealContainer,
    onPrimaryContainer = OnForestTealContainer,
    secondary = Terracotta,
    onSecondary = Color.White,
    secondaryContainer = TerracottaContainer,
    onSecondaryContainer = OnTerracottaContainer,
    tertiary = SaffronGold,
    onTertiary = Color.White,
    tertiaryContainer = SaffronContainer,
    onTertiaryContainer = OnSaffronContainer,
    background = ParchmentBg,
    onBackground = SlateInk,
    surface = ParchmentSurface,
    onSurface = SlateInk,
    surfaceVariant = ParchmentVariant,
    onSurfaceVariant = SlateMuted,
    outline = OutlineWarm,
    error = ErrorRed,
    errorContainer = ErrorContainer
)

private val DarkColorScheme = darkColorScheme(
    primary = ForestTealLight,
    onPrimary = ForestTealDark,
    primaryContainer = Color(0xFF15473F),
    onPrimaryContainer = ForestTealContainer,
    secondary = TerracottaLight,
    onSecondary = OnTerracottaContainer,
    secondaryContainer = Color(0xFF5C290B),
    onSecondaryContainer = TerracottaContainer,
    tertiary = Color(0xFFE6B655),
    onTertiary = OnSaffronContainer,
    tertiaryContainer = Color(0xFF4D3504),
    onTertiaryContainer = SaffronContainer,
    background = DarkBg,
    onBackground = DarkText,
    surface = DarkSurface,
    onSurface = DarkText,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkMuted,
    outline = DarkOutline,
    error = Color(0xFFEF9A9A),
    errorContainer = Color(0xFF5D1616)
)

@Composable
fun GramVidyaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
