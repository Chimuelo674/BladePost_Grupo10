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
import com.example.bladepost_grupo10.ui.HomeScreen // Asegúrate de que esta ruta es correcta
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

// 1. DEFINICIÓN DE RUTAS (SOLUCIÓN AL ERROR DE COMANDO)
object Screens {
    const val HOME_SCREEN = "home"
    const val DETAIL_SCREEN = "detail/{categoryId}"
}

// 2. LA NUEVA PÁGINA (DETAILSCREEN)
// Nota: Tu código original hacía una importación, pero es más limpio
// definirla aquí si es una pantalla sencilla.
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


// 3. MAINACTIVITY CORREGIDO
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BladePost_Grupo10Theme {
                // 🚀 Llamamos directamente a AppNavigator para que maneje las pantallas
                AppNavigator()
            }
        }
    }
}


// 4. APPNAVIGATOR (Sin cambios en su lógica)
@Composable
fun AppNavigator() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screens.HOME_SCREEN
    ) {
        composable(Screens.HOME_SCREEN) {
            // Pasamos el NavController a HomeScreen
            // ¡Asegúrate de que HomeScreen acepte NavHostController!
            HomeScreen(navController = navController)
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


// 5. COMPONENTES EXTRA (Sin cambios))
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