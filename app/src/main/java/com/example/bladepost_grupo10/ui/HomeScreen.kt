package com.example.bladepost_grupo10.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import com.example.bladepost_grupo10.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.bladepost_grupo10.Screens
import androidx.compose.material3.CardDefaults
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.Alignment

//1. DEFINICIONES DE DATOS Y TEMA (Sin cambios))

data class Category(val id: Int, val name: String, val color: Color)
data class ForumItem(val id: Int, val user: String, val comment: String, val categoryId: Int)
// Datos de ejemplo para Comentarios o Preguntas Frecuentes

val dummyForumItems = listOf(
    ForumItem(201, "BladeMaestro", "¿Cómo consigo el Bey de Ataque Feroz?", 1),
    ForumItem(202, "SpinKing", "Mi Bey de Defensa es imbatible, ¿algún consejo?", 2),
    ForumItem(203, "CustomGod", "Necesito ideas para personalizar mi Bey de Resistencia.", 3),
    ForumItem(204, "BalancePro", "Consejos para dominar el Beyblade de Balance Total.", 4)
)

val dummyCategories = listOf(
    Category(1, "Ataque Feroz", Color(0xFFFF4500)),
    Category(2, "Defensa Infranqueable", Color(0xFF1E90FF)),
    Category(3, "Resistencia Infinita", Color(0xFF3CB371)),
    Category(4, "Balance Total", Color(0xFFFFD700)),
    Category(5, "Customización", Color(0xFFBA55D3)),
)

private val DarkColorScheme = androidx.compose.material3.darkColorScheme(
    primary = Color(0xFFBB86FC),
    onPrimary = Color.Black,
    surface = Color.Black,
    onSurface = Color.White
)

private val LightColorScheme = androidx.compose.material3.lightColorScheme(
    primary = Color(0xFF6200EE),
    onPrimary = Color.White,
    surface = Color.White,
    onSurface = Color.Black
)

@Composable
fun AppTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = MaterialTheme.typography,
        content = content
    )
}

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

// 2. FUNCIÓN DE PANTALLA PRINCIPAL (HomeScreen)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    // ESTADOS
    var isDarkTheme by remember { mutableStateOf(false)}
    var isMenuExpanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }

    // ESTADOS DE BÚSQUEDA AÑADIDOS
    var isSearchVisible by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    AppTheme(darkTheme = isDarkTheme) {
        Scaffold(
            topBar = {

                // LÓGICA CONDICIONAL: Mostrar TopAppBar normal O TopAppBar de búsqueda
                if (isSearchVisible) {
                    // --- BARRA DE BÚSQUEDA ACTIVA ---
                    TopAppBar(
                        title = {
                            OutlinedTextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                placeholder = { Text("Buscar categorías...") },
                                // El TextField ocupa la mayor parte del espacio de Title
                                modifier = Modifier.fillMaxWidth(0.8f)
                            )
                        },
                        navigationIcon = {
                            // Icono para volver atrás y cerrar la búsqueda
                            IconButton(onClick = {
                                isSearchVisible = false // Cierra la búsqueda
                                searchQuery = ""        // Limpia el texto
                            }) {
                                Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                            }
                        },
                        actions = {
                            // Icono para limpiar el texto dentro del modo búsqueda
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
                                    println("\"Título de la app clickeado: Volviendo al inicio.")
                                    selectedCategory = null
                                }
                            )
                        },
                        navigationIcon = {
                            // Menú Desplegable de Categorías (Icono de Hamburguesa)
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
                            // Switch de Tema
                            Switch(
                                checked = isDarkTheme,
                                onCheckedChange = { isDarkTheme = it }
                            )
                            Text(
                                text = if (isDarkTheme) "Oscuro" else "Claro",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(end = 8.dp),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            // BOTÓN QUE ACTIVA EL MODO BÚSQUEDA
                            IconButton(onClick = { isSearchVisible = true }) {
                                Icon(
                                    imageVector = Icons.Filled.Search,
                                    contentDescription = "Activar Búsqueda",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    )
                } // Fin de la lógica condicional de TopAppBar
            }
        ){ innerPadding ->

            // LÓGICA DE FILTRADO APLICADA AL CONTENIDO PRINCIPAL
            val filteredCategories = dummyCategories.filter {
                // Filtra solo si hay un texto de búsqueda (searchQuery)
                if (searchQuery.isBlank()) {
                    true // Muestra todas si la búsqueda está vacía
                } else {
                    // Filtra por nombre de categoría (ignorando mayúsculas/minúsculas)
                    it.name.contains(searchQuery, ignoreCase = true)
                }
            }

            // CONTENIDO PRINCIPAL
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    text = "¡Bienvenido!",
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Explora Categorías",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 8.dp),
                    color = MaterialTheme.colorScheme.onSurface
                )

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    // USAR LA LISTA FILTRADA EN EL LAZYROW
                    items(filteredCategories) { category ->
                        CategoryChip(category = category) { clickedCategory ->
                            println("Categoría clickeada: ${clickedCategory.name}")
                            // Lógica de Navegación
                            navController.navigate("detail/${clickedCategory.id}")
                        }
                    }
                }

                // Muestra un mensaje si no hay resultados y la búsqueda está activa
                if (filteredCategories.isEmpty() && isSearchVisible) {
                    Text(
                        text = "No se encontraron categorías para \"$searchQuery\".",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(onClick = {
                    // VINCULACIÓN AL FORO:
                    navController.navigate(Screens.FORUM_SCREEN)
                }) {
                    Text("Crear Pregunta") // Texto actualizado
                }

                // --- Bloque de Imágenes de Noticias ---
                Image(
                    painter = painterResource(id = R.drawable.noticia_resistencia),
                    contentDescription = "resistencia",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentScale = ContentScale.Fit
                )
                Image(
                    painter = painterResource(id = R.drawable.noticia_prohibida),
                    contentDescription = "Prohibido",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentScale = ContentScale.Fit
                )
                Image(
                    painter = painterResource(id = R.drawable.noticia_balance),
                    contentDescription = "balance",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentScale = ContentScale.Fit
                )
                // --- Fin de Bloque de Imágenes de Noticias ---

                Spacer(modifier = Modifier.height(20.dp))

                // SECCIÓN: Últimos del Foro
                Text(
                    text = "Últimos Comentarios del Foro",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                // LISTA ESTÁTICA DE COMENTARIOS/PREGUNTAS
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    dummyForumItems.forEach { forumItem ->
                        ForumCard(item = forumItem)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp)) // Espacio antes del logo

                // AJUSTE DEL TAMAÑO Y POSICIONAMIENTO DEL LOGO (Al final de la pantalla)
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo App",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp), // Tamaño más pequeño para el logo de cierre
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.height(24.dp)) // Espacio final
                Spacer(modifier = Modifier.height(30.dp))
                Text(
                    text = "🔥 Eventos de la Comunidad",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
// Nueva Imagen o Banner
                Image(
                    painter = painterResource(id = R.drawable.nuevo_evento), // Asegúrate de tener este recurso
                    contentDescription = "Nuevo Evento",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentScale = ContentScale.Crop // Usar Crop para llenar el espacio
                )
// Texto explicativo opcional
                Text(
                    text = "¡Participa en el torneo semanal y gana piezas raras!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "🥇 Top 5 Bladers de la Semana",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
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
            }
        }
    }
}
@Composable
fun ForumCard(item: ForumItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = item.user,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = item.comment,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Categoría: ${dummyCategories.find { it.id == item.categoryId }?.name ?: "N/A"}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// 3. HOME SCREEN PREVIEW CORREGIDO

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview(){
    // CORRECCIÓN: Creamos un NavHostController ficticio para el Preview
    val mockNavController = rememberNavController()
    HomeScreen(navController = mockNavController)
}