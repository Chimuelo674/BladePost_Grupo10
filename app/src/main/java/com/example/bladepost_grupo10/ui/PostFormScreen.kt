package com.example.bladepost_grupo10.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostFormScreen(navController: NavHostController) {
    var questionText by remember { mutableStateOf("") }
    var titleText by remember { mutableStateOf("") }

    // 🚀 HARDCODED USERNAME: Reemplazar con el nombre real del usuario logeado
    val currentUserName = "Usuario_Autenticado_123"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Nueva Pregunta") },
                navigationIcon = {
                    // Botón para volver (regresa a ForumScreen)
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top)
        ) {

            // 🚀 MUESTRA EL NOMBRE DEL USUARIO QUE ESTÁ PUBLICANDO
            Text(
                text = "Publicando como: ${currentUserName}",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth()
            )

            Divider()

            OutlinedTextField(
                value = titleText,
                onValueChange = { titleText = it },
                label = { Text("Título de la pregunta") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = questionText,
                onValueChange = { questionText = it },
                label = { Text("Describe tu pregunta...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    // Lógica para enviar la pregunta
                    println("Pregunta enviada: Título='${titleText}' por el usuario '${currentUserName}'")
                    // 💡 LÓGICA FUTURA: Aquí enviarías {titleText, questionText, currentUserName} a tu servidor.
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = titleText.isNotBlank() && questionText.isNotBlank()
            ) {
                Text("Publicar Pregunta")
            }
        }
    }
}