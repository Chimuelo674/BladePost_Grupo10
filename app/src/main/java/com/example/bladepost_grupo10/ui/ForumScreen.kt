package com.example.bladepost_grupo10.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.bladepost_grupo10.Screens // Importa las rutas globales

// 1. MODELO DE DATOS PARA EL POST
data class ForumPost(
    val id: Int,
    val userName: String, // 🚀 Campo para el nombre de usuario
    val title: String,
    val question: String
)

// Datos de prueba para simular posts
val dummyPosts = listOf(
    ForumPost(1, "Ana_Tec", "Problema con Compose", "Mi Composable no se recompone cuando cambio el estado. ¿Alguna idea?"),
    ForumPost(2, "BladeFan10", "Error en Kotlin", "Necesito ayuda con una excepción que obtengo al desestructurar en mi bucle for."),
    ForumPost(3, "DeveloperX", "Duda de Navegación", "Cómo paso argumentos complejos entre pantallas de Compose Navigation? Hay alguna alternativa a NavType?"),
    ForumPost(4, "User_42", "Backend Question", "¿Qué base de datos recomiendan para un proyecto de foro pequeño en Android?")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForumScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Foro de Preguntas") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        // Botón Flotante para ir al Formulario de Nuevo Post
        floatingActionButton = {
            FloatingActionButton(onClick = {
                // Navegar al formulario de creación (asumiendo que POST_FORM_SCREEN existe)
                navController.navigate(Screens.POST_FORM_SCREEN)
            }) {
                Icon(Icons.Filled.Add, contentDescription = "Nueva pregunta")
            }
        }
    ) { paddingValues ->
        if (dummyPosts.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Aún no hay preguntas. ¡Sé el primero!", style = MaterialTheme.typography.titleMedium)
            }
        } else {
            // 2. Lista de Posts
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(dummyPosts) { post ->
                    ForumPostCard(post = post)
                }
            }
        }
    }
}

// 3. Composable para mostrar un solo Post
@Composable
fun ForumPostCard(post: ForumPost) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Navegar al detalle del post */ }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // 🚀 MOSTRAR NOMBRE DE USUARIO
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Preguntado por: ${post.userName}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
                // Opcional: Mostrar ID o fecha aquí
                Text(
                    text = "#${post.id}",
                    style = MaterialTheme.typography.labelSmall
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Título de la Pregunta
            Text(
                text = post.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))

            // Cuerpo de la Pregunta (Snippet)
            Text(
                text = post.question.take(100) + if (post.question.length > 100) "..." else "",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}