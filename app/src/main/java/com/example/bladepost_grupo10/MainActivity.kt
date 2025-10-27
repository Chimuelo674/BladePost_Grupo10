package com.example.bladepost_grupo10

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import com.example.bladepost_grupo10.ui.theme.BladePost_Grupo10Theme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Alignment
import androidx.navigation.NavHostController
import com.example.bladepost_grupo10.ui.DetailScreen
import com.example.bladepost_grupo10.ui.LoginScreen
import com.example.bladepost_grupo10.ui.RegistroScreen
import com.example.bladepost_grupo10.ui.HomeScreen
import com.example.bladepost_grupo10.ui.ForumScreen
import com.example.bladepost_grupo10.ui.PostFormScreen
import com.example.bladepost_grupo10.ui.PerfilScreen

//La línea que causaba el error se ELIMINA: private val Screens.Companion.LOGIN_SCREEN: Any


// 1. DEFINICIÓN DE RUTAS CON LOGIN INCLUIDO (CONSOLIDADO)
// Nota: Si este objeto está en otro archivo, DEBES borrar este bloque y usar solo las importaciones.
object Screens {
    const val PROFILE_SCREEN = "profile"
    const val LOGIN_SCREEN = "login" // 🚀 RUTA DE LOGIN AGREGADA
    const val HOME_SCREEN = "home"

    const val REGISTER_SCREEN ="register"
    const val FORUM_SCREEN = "forum"

    const val  POST_FORM_SCREEN = "post_from"
    const val DETAIL_SCREEN = "detail/{categoryId}"




}

// 2. LA NUEVA PÁGINA (DETAILSCREEN) - Sin cambios, está correcta aquí.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavHostController, categoryId: Int?) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de Categoría ${categoryId ?: ""}") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "ID de Categoría: ${categoryId ?: "N/A"}",
                style = MaterialTheme.typography.headlineMedium
            )
            Button(onClick = { navController.popBackStack() }) {
                Text("Volver a Inicio")
            }
        }
    }
}


// 3. MAINACTIVITY CORREGIDO - Sin cambios
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


// 4. APPNAVIGATOR - Sin cambios, ahora que Screens está corregido, esta función es válida.
@Composable
fun AppNavigator() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screens.LOGIN_SCREEN // Ahora sabe qué es LOGIN_SCREEN
    ) {
        composable(Screens.LOGIN_SCREEN){
            LoginScreen(navController = navController)
        }
        composable(Screens.HOME_SCREEN) {
            HomeScreen(navController = navController)
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
        composable(Screens.POST_FORM_SCREEN) {
            PostFormScreen(navController = navController)
        }
        composable(
            Screens.DETAIL_SCREEN,
            arguments = listOf(navArgument("categoryId") { type = NavType.IntType })
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getInt("categoryId")
            DetailScreen(navController = navController, categoryId = categoryId)
        }

    }
}


// 5. COMPONENTES EXTRA (Sin cambios)
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
    BladePost_Grupo10Theme {
        Greeting("Android")
    }
}