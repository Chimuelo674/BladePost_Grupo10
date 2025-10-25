package com.example.bladepost_grupo10

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bladepost_grupo10.ui.theme.BladePost_Grupo10Theme
import com.example.bladepost_grupo10.ui.HomeScreen
import com.example.bladepost_grupo10.ui.DetailScreen
import com.example.bladepost_grupo10.ui.ForumScreen
import com.example.bladepost_grupo10.ui.LoginScreen
import com.example.bladepost_grupo10.ui.PostFormScreen
import com.example.bladepost_grupo10.ui.RegistroScreen

// --- 1. DEFINICIÓN DE RUTAS ---
object Screens {
    const val PROFILE_SCREEN = "profile"
    const val LOGIN_SCREEN = "login"
    const val HOME_SCREEN = "home"
    const val DETAIL_SCREEN = "detail/{categoryId}" // Ruta con argumento
    const val REGISTER_SCREEN = "register"
    const val FORUM_SCREEN = "forum"
    const val POST_FORM_SCREEN = "post_form"
}

// --- 2. MAIN ACTIVITY ---
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BladePost_Grupo10Theme {
                AppNavigator()
            }
        }
    }
}

// --- 3. APP NAVIGATOR (Configuración del NavHost) ---
@Composable
fun AppNavigator() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screens.LOGIN_SCREEN // Pantalla de inicio
    ) {
        // RUTA DE LOGIN
        composable(Screens.LOGIN_SCREEN){
            LoginScreen(navController = navController)
        }

        // RUTA DE REGISTRO
        composable (Screens.REGISTER_SCREEN) {
            RegistroScreen(navController = navController)
        }

        // RUTA DE INICIO
        composable(Screens.HOME_SCREEN) {
            HomeScreen(navController = navController)
        }

        // 🚀 RUTA DE DETALLE (TU VERSIÓN)
        composable(Screens.DETAIL_SCREEN) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId")?.toIntOrNull()
            DetailScreen(navController = navController, categoryId = categoryId)
        }

        // RUTA DEL FORO
        composable(Screens.FORUM_SCREEN){
            ForumScreen(navController = navController)
        }

        // RUTA DEL FORMULARIO DE PUBLICACIÓN
        composable(Screens.POST_FORM_SCREEN){
            PostFormScreen(navController = navController)
        }
    }
}