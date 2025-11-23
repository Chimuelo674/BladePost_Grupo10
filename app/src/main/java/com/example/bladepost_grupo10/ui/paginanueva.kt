package com.example.bladepost_grupo10.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.bladepost_grupo10.R // Asegúrate de tener esta importación
import com.example.bladepost_grupo10.Screens // Importa las rutas globales
import com.example.bladepost_grupo10.ui.theme.AppTheme

// -- 1. ESTRUCTURAS DE DATOS --

// Estructura para el contenido que lleva imagen
data class DetailItem(val title: String, val description: String, val imageResId: Int)

//  MAPA DE CONTENIDO VISUAL ASOCIADO A CADA CATEGORÍA (COMPLETO Y CON IMÁGENES)
val dynamicDetailContent = mapOf(
    // 1: ATAQUE FEROZ
    1 to listOf(
        DetailItem(
            title = "Beyblade de Ataque Clave",
            description = "El diseño de este Beyblade prioriza la velocidad y el impacto. Sus bordes afilados buscan desviar al oponente y lograr un final rápido. Es vulnerable a la defensa prolongada.",
            imageResId = R.drawable.noticia_resistencia // Imagen
        ),
        DetailItem(
            title = "Estrategias de Arena",
            description = "Lanza fuerte y en ángulo para asegurar un patrón de ataque agresivo alrededor de la arena. Evita el centro.",
            imageResId = R.drawable.noticia_balance // Imagen
        )
    ),
    // 2: DEFENSA INFRANQUEABLE
    2 to listOf(
        DetailItem(
            title = "Beyblade de Defensa Superior",
            description = "Construido con peso distribuido uniformemente y una punta plana, este Beyblade se queda en el centro, absorbiendo ataques y conservando energía. Es pesado y lento.",
            imageResId = R.drawable.noticia_prohibida // Imagen
        ),
        DetailItem(
            title = "Técnicas de Absorción",
            description = "Lánzalo suavemente y déjalo en el centro. Su trabajo es resistir y desgastar la energía del atacante.",
            imageResId = R.drawable.noticia_resistencia // Imagen
        )
    ),
    // 3: RESISTENCIA INFINITA 🟢 ¡IMÁGENES AÑADIDAS!
    3 to listOf(
        DetailItem(
            title = "Anillo de Resistencia Larga",
            description = "Estos Beyblades están diseñados para durar. Poseen un anillo de energía bajo y una punta que minimiza la fricción, enfocándose en la conservación de giro.",
            imageResId = R.drawable.noticia_resistencia // Imagen
        ),
        DetailItem(
            title = "Estrategia: Espera y Gana",
            description = "Lánzalo suavemente y hacia el centro. Su objetivo no es atacar, sino sobrevivir. Es ideal contra tipos de Ataque que se agotan rápido.",
            imageResId = R.drawable.noticia_balance // Imagen
        )
    ),
    // 4: BALANCE TOTAL 🟢 ¡IMÁGENES AÑADIDAS!
    4 to listOf(
        DetailItem(
            title = "Diseño Híbrido",
            description = "Los Beyblades de Balance combinan características de los tres tipos. Su versatilidad les permite adaptarse a diferentes oponentes y estrategias en la arena.",
            imageResId = R.drawable.noticia_balance // Imagen
        ),
        DetailItem(
            title = "La Clave es la Adaptación",
            description = "Dependiendo del oponente, ajusta tu fuerza y ángulo de lanzamiento. Puede ser defensivo o atacante según la situación.",
            imageResId = R.drawable.noticia_prohibida // Imagen
        )
    ),
    // 5: CUSTOMIZACIÓN 🟢 ¡IMÁGENES AÑADIDAS!
    5 to listOf(
        DetailItem(
            title = "El Arte de Combinar Piezas",
            description = "Esta categoría se centra en la optimización de partes (anillos, núcleos y drivers) para crear un Beyblade único. ¡La imaginación es el límite!",
            imageResId = R.drawable.noticia_prohibida // Imagen
        ),
        DetailItem(
            title = "Consejos de Piezas Raras",
            description = "Las piezas de edición limitada suelen ofrecer ventajas estadísticas clave, pero requieren mucha práctica para dominarlas.",
            imageResId = R.drawable.noticia_resistencia // Imagen
        )
    )
)

// -- 2. COMPONENTE CARD PARA CONTENIDO DETALLADO --
@Composable
fun DetailContentCard(item: DetailItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            //  AQUÍ SE MUESTRA LA IMAGEN USANDO EL ID DEL RECURSO
            Image(
                painter = painterResource(id = item.imageResId),
                contentDescription = item.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Título
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Descripción
            Text(
                text = item.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}


// -- 3. PANTALLA PRINCIPAL DE DETALLE --
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavHostController, categoryId: Int?) {
    // Si dummyCategories está definido en HomeScreen.kt, debe ser accesible o importado.
    // Asumiendo que Category y dummyCategories están accesibles/en el mismo paquete
    val categoryName = dummyCategories.find { it.id == categoryId }?.name ?: "Categoría Desconocida"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(" $categoryName") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                ), // Padding alrededor de la tarjeta principal
            contentAlignment = Alignment.TopCenter
        ) {
            //  CAMBIO CLAVE: Card para la sección de detalles
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(
                    // Fondo de la tarjeta: color de superficie (claro/blanco) para contraste
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.Start
                ) {
                    //--- 1. SECCIÓN PRINCIPAL ---
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = " $categoryName",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Aquí encontrarás las partes clave, estrategias y consejos para dominar este tipo de Beyblade.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    // --- 2. CONTENIDO DINÁMICO ---
                    Spacer(modifier = Modifier.height(24.dp))
                    Spacer(modifier = Modifier.height(8.dp))

                    //  Búsqueda dinámica del contenido visual basado en el categoryId
                    val visualContent = dynamicDetailContent[categoryId] ?: emptyList()

                    // Itera sobre el contenido específico de la categoría
                    if (visualContent.isNotEmpty()) {
                        visualContent.forEach { item ->
                            DetailContentCard(item = item)
                        }
                    } else {
                        Text(
                            text = "No hay contenido visual específico para esta categoría.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    // -----------------------------------------------------------

                    //  ESPACIADOR CLAVE: Empuja el botón al final de la columna desplazable.
                    Spacer(modifier = Modifier.weight(1f))

                    Spacer(modifier = Modifier.height(32.dp))

                    //  BOTÓN AL FINAL
                    Button(
                        onClick = { navController.navigate(Screens.HOME_SCREEN) },
                        modifier = Modifier.fillMaxWidth().height(50.dp)
                    ) {
                        Text("Volver a Inicio")
                    }
                    Spacer(modifier = Modifier.height(16.dp)) // Padding inferior
                }
            }
        }
    }
}
// --- 4. PREVIEW ---
@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    val mockNavController = rememberNavController()
    AppTheme(darkTheme = false) {
        // Previsualiza la categoría de Resistencia Infinita (ID 3)
        DetailScreen(navController = mockNavController, categoryId = 3)


    }
}