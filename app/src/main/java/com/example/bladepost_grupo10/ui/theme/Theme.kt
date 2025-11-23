package com.example.bladepost_grupo10.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// --- Importa tus colores base de Color.kt ---
import com.example.bladepost_grupo10.ui.theme.Purple80
import com.example.bladepost_grupo10.ui.theme.PurpleGrey80
import com.example.bladepost_grupo10.ui.theme.Pink80
import com.example.bladepost_grupo10.ui.theme.Purple40
import com.example.bladepost_grupo10.ui.theme.PurpleGrey40
import com.example.bladepost_grupo10.ui.theme.Pink40
import com.example.bladepost_grupo10.ui.theme.VibrantSunsetOrange
import com.example.bladepost_grupo10.ui.theme.Typography

// 🚀 Tonalidades de Fondo Ajustadas
val DarkSurface = Color(0xFF1E1E1E)
val DarkBackground = Color(0xFF121212)
val LightSurface = Color(0xFFFAFAFA)
val LightTextOnDark = Color(0xFFFFFFFF) // Blanco
val DarkTextOnLight = Color(0xFF000000) // Negro

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,

    // MODO OSCURO: Fondo y Superficies oscuros
    background = DarkBackground,
    surface = DarkSurface,
    onBackground = LightTextOnDark,
    onSurface = LightTextOnDark
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,

    // ✅ CORRECCIÓN CLAVE: Fondo NARANJA atractivo en modo CLARO
    background = VibrantSunsetOrange,
    surface = LightSurface,
    onBackground = DarkTextOnLight,   // Texto sobre fondo naranja (negro para contraste)
    onSurface = DarkTextOnLight
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // 🚨 Desactivar DynamicColor para asegurar que el fondo NARANJA fijo se aplique
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        // La lógica DynamicColor se salta porque dynamicColor es false

        darkTheme -> DarkColorScheme
        else -> LightColorScheme // <--- Usa el tema con fondo Naranja
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()

            // Lógica para que los iconos de la barra de estado sean legibles:
            // Si el fondo del tema es CLARO (Naranja), los iconos deben ser OSCUROS (true).
            val isBackgroundLight = colorScheme.background == VibrantSunsetOrange

            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = isBackgroundLight
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}