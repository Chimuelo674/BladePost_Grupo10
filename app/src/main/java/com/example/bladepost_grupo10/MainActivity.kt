package com.example.bladepost_grupo10

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface // Importación necesaria
import com.example.bladepost_grupo10.ui.DetailScreen
import com.example.bladepost_grupo10.ui.LoginScreen
import com.example.bladepost_grupo10.ui.RegistroScreen
import com.example.bladepost_grupo10.ui.HomeScreen
import com.example.bladepost_grupo10.ui.ForumScreen
import com.example.bladepost_grupo10.ui.PostFormScreen
import com.example.bladepost_grupo10.ui.PerfilScreen
import com.example.bladepost_grupo10.ui.theme.AppTheme
import androidx.compose.runtime.getValue // Importación necesaria
import androidx.compose.runtime.mutableStateOf // Importación necesaria
import androidx.compose.runtime.remember // Importación necesaria
import androidx.compose.runtime.setValue // Importación necesaria


// 1. DEFINICIÓN DE RUTAS (Screens)
object Screens {
    const val PROFILE_SCREEN = "profile"
    const val LOGIN_SCREEN = "login"
    const val HOME_SCREEN = "home"
    const val REGISTER_SCREEN = "register"
    const val FORUM_SCREEN = "forum"
    const val POST_FORM_SCREEN = "post_from"
    const val DETAIL_SCREEN = "detail/{categoryId}"
}

// 2. FUNCIÓN DE NAVEGACIÓN (AppNavigation/AppNavigator)
// Esta es la función principal que maneja las rutas y el estado del tema
@Composable
fun AppNavigation(
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit
) {
    val navController = rememberNavController()

    // 🔑 CORRECCIÓN: SIMULACIÓN DEL USUARIO AUTENTICADO
    var loggedInUser by remember { mutableStateOf("Usuario_Logeado_EJEMPLO") }

    NavHost(
        navController = navController,
        startDestination = Screens.LOGIN_SCREEN
    ) {
        composable(Screens.LOGIN_SCREEN){
            LoginScreen(navController = navController)
        }
        composable(Screens.HOME_SCREEN) {
            // Pasamos el control del tema solo a las pantallas que lo necesitan
            HomeScreen(
                navController = navController,
                isDarkTheme = isDarkTheme,
                onThemeChange = onThemeChange
            )
        }
        composable(Screens.PROFILE_SCREEN) {
            PerfilScreen(navController = navController)
        }
        composable(Screens.REGISTER_SCREEN ) {
            RegistroScreen(navController= navController)
        }
        composable(Screens.FORUM_SCREEN) {
            ForumScreen(navController = navController)
        }
        // ✅ CORRECCIÓN APLICADA: Pasamos el parámetro currentUserName
        composable(Screens.POST_FORM_SCREEN) {
            PostFormScreen(
                navController = navController
            )
        }
        composable(
            Screens.DETAIL_SCREEN,
            arguments = listOf(navArgument("categoryId") { type = NavType.IntType })
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getInt("categoryId")
            // Usamos la DetailScreen importada de ui/
            DetailScreen(navController = navController, categoryId = categoryId)
        }

    }
}


//3. LA CLASE PRINCIPAL (MainActivity)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            // 💡 ESTADO DEL TEMA CENTRAL
            var isAppDarkTheme by remember { mutableStateOf(false) }

            // 🔑 APLICA EL TEMA (AppTheme definido en ui/)
            AppTheme(darkTheme = isAppDarkTheme) {

                // ✅ SOLUCIÓN DEL FONDO BLANCO: Surface con color de fondo del tema
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // LLAMA A LA NAVEGACIÓN, PASANDO EL ESTADO DEL TEMA
                    AppNavigation(
                        isDarkTheme = isAppDarkTheme,
                        onThemeChange = { isAppDarkTheme = it }
                    )
                }
            }
        }
    }
}


// 4. COMPONENTES EXTRA (Previews)
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Bienvenido A Blade Post",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    // ✅ CORRECCIÓN: Usar el nombre de la función correcta (AppTheme)
    AppTheme(darkTheme = false) {
        Greeting("Android")
    }
}