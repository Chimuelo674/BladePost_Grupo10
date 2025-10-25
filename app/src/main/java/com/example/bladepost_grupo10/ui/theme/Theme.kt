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

// 🚀 IMPORTACIÓN CLAVE: Agrega esta línea
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,

    // 🚀 CAMBIOS CLAVE PARA FONDO NEGRO
    background = Color.Black, // Fondo principal: NEGRO
    surface = Color.Black,    // Superficies como Cards o Scaffolds: NEGRO
    onBackground = Color.White, // Color del texto sobre el fondo: BLANCO
    onSurface = Color.White     // Color del texto sobre superficies: BLANCO
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,

    // 🚀 CAMBIOS CLAVE PARA FONDO BLANCO
    background = Color.White,  // Fondo principal: BLANCO
    surface = Color.White,     // Superficies como Cards o Scaffolds: BLANCO
    onBackground = Color.Black,  // Color del texto sobre el fondo: NEGRO
    onSurface = Color.Black      // Color del texto sobre superficies: NEGRO

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
fun BladePost_Grupo10Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
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
        typography = Typography,
        content = content
    )
}