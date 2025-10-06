package com.example.bladepost_grupo10.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import com.example.bladepost_grupo10.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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

// 1. DEFINICIONES DE DATOS Y TEMA (Sin cambios))

data class Category(val id: Int, val name: String, val color: Color)

val dummyCategories = listOf(
    Category(1, "Deportes", Color(0xFFE91E63)),
    Category(2, "Noticias", Color(0xFF00BCD4)),
    Category(3, "Tecnología", Color(0xFFFF9800)),
    Category(4, "Comida", Color(0xFF4CAF50)),
    Category(5, "Viajes", Color(0xFF3F51B5)),
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

// 2. FUNCIÓN DE PANTALLA PRINCIPAL (HomeScreen) - Sin cambios

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
                                placeholder = { Text("Buscar...") },
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
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
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
                            Text("Blade Post APP" + (selectedCategory?.let { " - ${it.name}" } ?: ""),
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.clickable {
                                    println("\"Título de la app clickeado: Volviendo al inicio.")
                                    selectedCategory = null
                                }
                            )
                        },
                        navigationIcon = {
                            // Menú Desplegable de Categorías
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
                                            println("Categoría seleccionada: ${category.name}")
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
            // CONTENIDO PRINCIPAL
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 16.dp),
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
                    items(dummyCategories) { category ->
                        CategoryChip(category = category) { clickedCategory ->
                            println("Categoría clickeada: ${clickedCategory.name}")
                            // Lógica de Navegación
                            navController.navigate("detail/${clickedCategory.id}")
                        }
                    }
                }
                Button(onClick = { }) {
                    Text("Presióname")
                }





                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo App",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentScale = ContentScale.Fit
                )


            }
        }
    }
}

// 3. HOME SCREEN PREVIEW CORREGIDO

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview(){
    // 💡 CORRECCIÓN: Creamos un NavHostController ficticio para el Preview
    val mockNavController = rememberNavController()
    HomeScreen(navController = mockNavController)
}



