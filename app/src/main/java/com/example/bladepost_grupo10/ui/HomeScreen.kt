package com.example.bladepost_grupo10.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import com.example.bladepost_grupo10.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Switch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material.icons.filled.Close
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.bladepost_grupo10.Screens
import androidx.compose.material3.CardDefaults
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.Alignment
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material.icons.filled.ChatBubbleOutline // Icono de comentarios
import androidx.compose.foundation.layout.size
import androidx.compose.ui.draw.clip // Para usar clip con RoundedCornerShape
import com.example.bladepost_grupo10.ui.forumPostsState // ✅ ESTADO REACTIVO
import com.example.bladepost_grupo10.ui.ForumPost // ✅ MODELO DE DATOS
// ❌ ELIMINADA: import com.example.bladepost_grupo10.ui.dummyPosts
// ------------------------------------
import com.example.bladepost_grupo10.ui.theme.AppTheme
import com.example.bladepost_grupo10.R.drawable.logo_nuevo // Importación de ejemplo de imagen

// --- 1. DEFINICIONES DE DATOS ---
// NOTA: Se asume que ForumPost fue movido a ForumScreen.kt (o un archivo compartido)

data class Category(val id: Int, val name: String, val color: Color)
data class NewsItem(val id: Int, val title: String, val summary: String, val imageUrl: Int)

// DATOS DE EJEMPLO
val dummyNews = listOf(
    NewsItem(101, "Lanzamiento: Nuevo Bey de Resistencia", "Analizamos el nuevo modelo que promete un giro infinito.", R.drawable.noticia_resistencia),
    NewsItem(102, "Claves para el balance total", "Las estrategias de balance que dominaron el torneo pasado.", R.drawable.noticia_balance),
    NewsItem(103, "Customización Prohibida", "Los drivers que la organización vetó por ser demasiado poderosos.", R.drawable.noticia_prohibida)
)

val dummyCategories = listOf(
    Category(1, "Ataque Feroz", Color(0xFFFF4500)),
    Category(2, "Defensa Infranqueable", Color(0xFF1E90FF)),
    Category(3, "Resistencia Infinita", Color(0xFF3CB371)),
    Category(4, "Balance Total", Color(0xFFFFD700)),
    Category(5, "Customización", Color(0xFFBA55D3)),
)

// --- Componibles de Soporte (CategoryChip) ---

@Composable
fun CategoryChip(category: Category, onCategoryClick: (Category) -> Unit) {
    Card(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .clickable { onCategoryClick(category) },
        shape = RoundedCornerShape(8.dp),
    ) {
        Column(
            modifier = Modifier
                .background(category.color)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = category.name,
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

// --- 2. FUNCIÓN DE PANTALLA PRINCIPAL (HomeScreen) ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    isDarkTheme: Boolean = false,
    onThemeChange: (Boolean) -> Unit = {}
) {
    // --- ESTADOS LOCALES MÍNIMOS ---
    var isMenuExpanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var isSearchVisible by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    val currentIsDarkTheme = isDarkTheme

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            if (isSearchVisible) {
                // --- BARRA DE BÚSQUEDA ACTIVA ---
                TopAppBar(
                    title = {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Buscar categorías...") },
                            modifier = Modifier.fillMaxWidth(0.8f),
                            textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface)
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            isSearchVisible = false
                            searchQuery = ""
                        }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                        }
                    },
                    actions = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Filled.Close, contentDescription = "Limpiar")
                            }
                        }
                    }
                )

            } else {
                // --- TOPAPPBAR NORMAL ---
                TopAppBar(
                    title = {
                        Text("Blade Post" + (selectedCategory?.let { " - ${it.name}" } ?: ""),
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.clickable {
                                selectedCategory = null
                            }
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { isMenuExpanded = true }) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = "Categoría",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        DropdownMenu(
                            expanded = isMenuExpanded,
                            onDismissRequest = { isMenuExpanded = false }
                        ){
                            dummyCategories.forEach { category ->
                                DropdownMenuItem(
                                    text = { Text(category.name, color = MaterialTheme.colorScheme.onSurface) },
                                    onClick = {
                                        selectedCategory = category
                                        isMenuExpanded = false
                                        navController.navigate("detail/${category.id}")
                                    }
                                )
                            }
                        }
                    },
                    actions = {
                        // SWITCH DE TEMA
                        Switch(
                            checked = currentIsDarkTheme,
                            onCheckedChange = onThemeChange
                        )
                        Text(
                            text = if (currentIsDarkTheme) "Oscuro" else "Claro",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(end = 8.dp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        IconButton(onClick = { navController.navigate(Screens.PROFILE_SCREEN) }) {
                            Icon(
                                imageVector = Icons.Filled.Person,
                                contentDescription = "Ir a Perfil",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        IconButton(onClick = { isSearchVisible = true }) {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = "Activar Búsqueda",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                )
            }
        }
    ){ innerPadding ->

        // LÓGICA DE FILTRADO
        val filteredCategories = dummyCategories.filter {
            if (searchQuery.isBlank()) {
                true
            } else {
                it.name.contains(searchQuery, ignoreCase = true)
            }
        }

        // CONTENIDO PRINCIPAL
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "¡Bienvenido!",
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Explora Categorías",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 8.dp),
                color = MaterialTheme.colorScheme.onBackground
            )

            LazyRow(
                modifier = Modifier.fillMaxWidth().height(50.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(filteredCategories) { category ->
                    CategoryChip(category = category) { clickedCategory ->
                        // Línea corregida: Usar la ruta de string directa
                        navController.navigate("detail/${clickedCategory.id}")
                    }
                }
            }


            if (filteredCategories.isEmpty() && isSearchVisible) {
                Text(
                    text = "No se encontraron categorías para \"$searchQuery\".",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // LOGO (CON TAMAÑO AUMENTADO A 160.dp)
            Image(
                painter = painterResource(id = R.drawable.logo_nuevo),
                contentDescription = "Logo App",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .padding(vertical = 16.dp),
                contentScale = ContentScale.Fit,
                alignment = Alignment.Center
            )

            // * ----------------------------------------------------
            // * --- CARRUSEL HORIZONTAL DE NOTICIAS (LazyRow) ---
            // * ----------------------------------------------------

            Text(
                text = "🔥 Noticias Destacadas",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 8.dp),
                color = MaterialTheme.colorScheme.onBackground
            )

            LazyRow(
                modifier = Modifier.fillMaxWidth().height(200.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = 0.dp)
            ) {
                items(dummyNews) { newsItem ->
                    Card(
                        modifier = Modifier
                            .width(300.dp)
                            .fillMaxHeight(),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            // 💡 NOTA: newsItem.imageUrl usa R.drawable.logo_nuevo. Cámbialo en dummyNews para ver diferentes imágenes.
                            Image(
                                painter = painterResource(id = newsItem.imageUrl),
                                contentDescription = newsItem.title,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            Column(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .background(Color.Black.copy(alpha = 0.6f))
                                    .padding(8.dp)
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = newsItem.title,
                                    color = Color.White,
                                    style = MaterialTheme.typography.titleSmall
                                )
                            }
                        }
                    }
                }
            }
            // * --------------------------------------------------

            Spacer(modifier = Modifier.height(20.dp))

            // SECCIÓN: Últimos del Foro
            Text(
                text = "Últimas 4 Preguntas del Foro",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            // ✅ LISTA DE POSTS: Lógica de imagen y comentarios
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // ✅ CAMBIO CLAVE: Usa el estado reactivo
                forumPostsState.take(4).forEach { post ->
                    Card(
                        modifier = Modifier.fillMaxWidth().clickable { navController.navigate(Screens.FORUM_SCREEN) },
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            // Título y Usuario
                            Text(
                                text = post.title,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "Por: ${post.userName}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            // ✅ IMAGEN (Se muestra SOLO si post.imageResId no es nulo)
                            if (post.imageResId != null) {
                                Image(
                                    painter = painterResource(id = post.imageResId),
                                    contentDescription = "Imagen de la pregunta",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(100.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }

                            // ✅ CONTEO DE COMENTARIOS
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.ChatBubbleOutline,
                                    contentDescription = "Comentarios",
                                    modifier = Modifier.size(16.dp),
                                    tint = MaterialTheme.colorScheme.tertiary
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${post.commentCount} Comentarios",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.tertiary
                                )
                            }

                            // ❌ BLOQUE REDUNDANTE ELIMINADO
                        }
                    }
                }
            }

            Button(onClick = {
                navController.navigate(Screens.FORUM_SCREEN)
            }) {
                Text("Ir al Foro")
            }

            Spacer(modifier = Modifier.height(20.dp))

            // SECCIÓN: Eventos de la Comunidad
            Text(
                text = "🔥 Eventos de la Comunidad",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            //✅ SECCIÓN DE IMAGEN DE EVENTO
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    // 💡 NOTA: R.drawable.logo_nuevo. Cámbialo por R.drawable.imagen_evento si tienes ese recurso.
                    Image(
                        painter = painterResource(R.drawable.nuevo_evento), // Se usó logo_nuevo como placeholder
                        contentDescription = "Imagen de Nuevo Evento",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            // Texto explicativo opcional
            Text(
                text = "¡Participa en el torneo semanal y gana piezas raras!",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(20.dp))

            // SECCIÓN: Top 5 Bladers
            Text(
                text = "🥇 Top 5 Bladers de la Semana",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(top = 8.dp)
            ) {
                items(listOf("Tyson", "Kai", "Ray", "Max", "Daichi")) { user ->
                    Card(
                        modifier = Modifier.width(100.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0E68C))
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp).fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Filled.Person, contentDescription = "Usuario", tint = Color.Black)
                            Text(user, style = MaterialTheme.typography.bodyLarge, color = Color.Black)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}


// --- 3. HOME SCREEN PREVIEW CORREGIDO ---

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview(){
    val mockNavController = rememberNavController()
    AppTheme(darkTheme = false) {
        HomeScreen(navController = mockNavController, isDarkTheme = false, onThemeChange = {})
    }
}