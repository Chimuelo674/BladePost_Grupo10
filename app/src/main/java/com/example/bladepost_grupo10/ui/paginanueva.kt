package com.example.bladepost_grupo10.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

//SE ELIMINA EL OBJETO SCREENS COMPLETO
/*
object Screens {
    val PROFILE_SCREEN: Any // LÍNEA 25: ESTO CAUSA EL ERROR
    const val HOME_SCREEN = "home"
    const val DETAIL_SCREEN = "detail/{categoryId}"
}
*/

// ----------------------------------------------------------------------
// FUNCIÓN DETAILSCREEN
// ----------------------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavHostController, categoryId: Int?) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de Categoría ${categoryId ?: ""}") },
                navigationIcon = {
                    // Botón para volver a la pantalla anterior
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
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