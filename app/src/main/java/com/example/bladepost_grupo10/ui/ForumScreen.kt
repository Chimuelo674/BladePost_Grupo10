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
import com.example.bladepost_grupo10.R // Necesario para acceder a recursos como drawables

// 1. MODELO DE DATOS PARA EL POST - ✅ Corregido y Final
data class ForumPost(
    val id: Int,
    val userName: String,
    val title: String,
    val question: String,
    // ✅ PROPIEDADES AGREGADAS
    val imageResId: Int? = null, // Puede ser nula si el post no tiene imagen
    val commentCount: Int = 0    // Contador de comentarios
)

// Datos de prueba para simular posts - ✅ Actualizados
val dummyPosts = listOf(
    // Post 1: CON IMAGEN
    ForumPost(
        id = 1,
        userName = "Ana_Tec",
        title = "Problema con Compose: ¿Recomposición infinita?",
        question = "Mi Composable no se recompone cuando cambio el estado. ¿Alguna idea?",
        imageResId = R.drawable.logo_nuevo, // 👈 USAR TU IMAGEN DE PREVIEW AQUÍ
        commentCount = 12
    ),
    // Post 2: SIN IMAGEN
    ForumPost(
        id = 2,
        userName = "BladeFan10",
        title = "Error en Kotlin: Uso incorrecto de 'when'",
        question = "Necesito ayuda con una excepción que obtengo al desestructurar en mi bucle for.",
        imageResId = null, // 👈 SIN IMAGEN
        commentCount = 5
    ),
    // Post 3: CON IMAGEN
    ForumPost(
        id = 3,
        userName = "DeveloperX",
        title = "¿Cómo paso argumentos entre pantallas de Compose Navigation?",
        question = "Cómo paso argumentos complejos entre pantallas de Compose Navigation? Hay alguna alternativa a NavType?",
        imageResId = R.drawable.logo_nuevo, // 👈 USAR TU IMAGEN DE PREVIEW AQUÍ
        commentCount = 21
    ),
    // Post 4: SIN IMAGEN
    ForumPost(
        id = 4,
        userName = "UsuarioRandom",
        title = "Duda sobre el sistema de puntos de experiencia (XP)",
        question = "Hay un bug en el contador de XP después de publicar un post. ¿Alguien más lo ha notado?",
        imageResId = null, // 👈 SIN IMAGEN
        commentCount = 3
    ),
    // Más posts
    ForumPost(5, "BladerMaestro", "Mejores Drivers para el Ataque Feroz", "He probado 5 drivers, aquí mi ranking...", commentCount = 15),
    ForumPost(6, "TheSniper", "Estrategias de Arena", "Posiciones y ángulos para usar la resistencia de forma efectiva.", commentCount = 8)
)


// 2. COMPONENTE ForumPostCard (Si existe, para que el post tenga imagen cuando se muestre)

@Composable
fun ForumPostCard(post: ForumPost, onPostClick: (ForumPost) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onPostClick(post) }
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = post.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Por: ${post.userName}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}


// 3. PANTALLA PRINCIPAL DEL FORO

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForumScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Foro de Discusión") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /* Navegar a la pantalla de nuevo post */ }) {
                Icon(Icons.Filled.Add, contentDescription = "Nuevo Post")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 8.dp, bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Text(
                    text = "Todos los Temas",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }
            items(dummyPosts) { post ->
                // NOTA: Usamos ForumPostCard, pero podrías usar la vista detallada que tenías si era más compleja.
                ForumPostCard(post = post) { clickedPost ->
                    // Navegar a la pantalla de detalle del post (si existe)
                    // navController.navigate(Screens.POST_DETAIL + "/${clickedPost.id}")
                }
            }
        }
    }
}