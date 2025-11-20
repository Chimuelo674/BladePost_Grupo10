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
import com.example.bladepost_grupo10.ui.theme.Typography // Importación para la tipografía

// 🚀 Tonalidades de Fondo Ajustadas (Fondo fijo)
val DarkSurface = Color(0xFF1E1E1E)
val DarkBackground = Color(0xFF121212) // Fondo fijo que deseamos
val LightSurface = Color(0xFFFAFAFA)
val LightTextOnDark = Color(0xFFFFFFFF) // Blanco para texto sobre fondo fijo

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

    // ✅ MODO CLARO MODIFICADO (FONDO FIJO OSCURO)
    background = DarkBackground,  // <--- ¡EL FONDO ES SIEMPRE OSCURO!
    surface = LightSurface,       // <--- Las Cards, TopBar, Switch, etc. tienen color CLARO
    onBackground = LightTextOnDark,   // <--- Texto sobre fondo oscuro: Blanco
    onSurface = Color.Black       // <--- Texto sobre superficies claras: Negro
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            // Nota: Aquí se usa el color dinámico, que podría sobreescribir el fondo fijo.
            // Para asegurar el fondo fijo, considera desactivar dynamicColor si es clave.
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme // <--- Usamos el LightColorScheme modificado
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()

            // Si el fondo es DarkBackground, los iconos de la barra deben ser Light (no oscuros)
            // Por eso usamos '!darkTheme' para controlar la apariencia de los iconos.
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}