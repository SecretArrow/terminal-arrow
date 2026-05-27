package com.terminalarrow.app.core.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// Brand palette — terminal-green accents on a deep navy canvas.
private val BrandPrimary = Color(0xFF00E676)
private val BrandPrimaryDark = Color(0xFF00C853)
private val BrandSecondary = Color(0xFF80DEEA)
private val BrandTertiary = Color(0xFFB388FF)
private val BrandBgDark = Color(0xFF0B1020)
private val BrandSurfaceDark = Color(0xFF11172A)
private val BrandSurfaceContainer = Color(0xFF161D32)

private val DarkColorScheme = darkColorScheme(
    primary = BrandPrimary,
    onPrimary = Color(0xFF003917),
    primaryContainer = Color(0xFF00531F),
    onPrimaryContainer = Color(0xFFA6F4C5),
    secondary = BrandSecondary,
    onSecondary = Color(0xFF003640),
    tertiary = BrandTertiary,
    onTertiary = Color(0xFF260055),
    background = BrandBgDark,
    onBackground = Color(0xFFE6EDF3),
    surface = BrandSurfaceDark,
    onSurface = Color(0xFFE6EDF3),
    surfaceVariant = BrandSurfaceContainer,
    onSurfaceVariant = Color(0xFFB4BCC8),
    outline = Color(0xFF6B7280),
    error = Color(0xFFFF6E6E)
)

private val LightColorScheme = lightColorScheme(
    primary = BrandPrimaryDark,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFB8F5C8),
    onPrimaryContainer = Color(0xFF003917),
    secondary = Color(0xFF00838F),
    onSecondary = Color.White,
    tertiary = Color(0xFF6A39CE),
    onTertiary = Color.White,
    background = Color(0xFFFBFCFE),
    onBackground = Color(0xFF1B1F23),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1B1F23),
    surfaceVariant = Color(0xFFEEF1F5),
    onSurfaceVariant = Color(0xFF44525E),
    outline = Color(0xFF8B95A1),
    error = Color(0xFFB3261E)
)

@Composable
fun TerminalArrowTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
