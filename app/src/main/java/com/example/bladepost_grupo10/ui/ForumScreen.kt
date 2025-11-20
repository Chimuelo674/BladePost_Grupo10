package com.example.bladepost_grupo10.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.bladepost_grupo10.Screens
// 🚀 IMPORTACIONES NECESARIAS PARA MOSTRAR LA IMAGEN
import android.net.Uri
import androidx.compose.foundation.Image
import coil.compose.rememberAsyncImagePainter
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
// ----------------------------------------------------

// 1. MODELO DE DATOS PARA EL POST - AHORA INCLUYE imageUri
data class ForumPost(
    val id: Int,
    val userName: String,
    val title: String,
    val question: String,
    // 🚀 NUEVO CAMPO PARA LA FOTO (Puede ser nulo si no se adjuntó)
    val imageUri: Uri? = null
)

//Datos de prueba para simular posts
val dummyPosts = listOf(
    ForumPost(1, "Ana_Tec", "Problema con Compose", "Mi Composable no se recompone cuando cambio el estado. ¿Alguna idea?", imageUri = null),
    ForumPost(2, "BladeFan10", "Error en Kotlin", "Necesito ayuda con una excepción que obtengo al desestructurar en mi bucle for.", imageUri = null),
    ForumPost(3, "DeveloperX", "Duda de Navegación", "Cómo paso argumentos complejos entre pantallas de Compose Navigation? Hay alguna alternativa a NavType?", imageUri = null),
    ForumPost(4, "CoderPro", "Performance en LazyColumn", "Tengo muchos elementos y la lista va lenta. ¿Consejos para optimizar?", imageUri = null),
    ForumPost(5, "UserTester", "Pregunta de diseño UI", "Alguien sabe cómo hacer un efecto de parallax suave en Jetpack Compose?", imageUri = null),
)

// --------------------------------------------------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForumScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Blade Post: Foro de Preguntas") },
                // Botón de Volver
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                // Botón para crear nuevo post
                actions = {
                    IconButton(onClick = { navController.navigate(Screens.POST_FORM_SCREEN) }) {
                        Icon(Icons.Filled.Add, contentDescription = "Crear Post")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 8.dp, bottom = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(dummyPosts) { post ->
                ForumPostCard(post = post, navController = navController)
            }
        }
    }
}

// --------------------------------------------------------------------------------------------------

@Composable
fun ForumPostCard(post: ForumPost, navController: NavHostController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Navegar al detalle del post */ }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // MOSTRAR NOMBRE DE USUARIO
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

            // 🚀 LÓGICA DE MOSTRAR IMAGEN ADJUNTA
            post.imageUri?.let { uri ->
                Spacer(modifier = Modifier.height(8.dp))
                Image(
                    painter = rememberAsyncImagePainter(model = uri),
                    contentDescription = "Imagen adjunta al post",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp) // Altura fija para la vista previa
                        .clip(MaterialTheme.shapes.small),
                    contentScale = ContentScale.Crop
                )
            }
            // ----------------------------------------------------
        }
    }
}