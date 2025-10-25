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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.bladepost_grupo10.R // Asegúrate de tener esta importación

// --- 1. ESTRUCTURAS DE DATOS ---

// Estructura para el contenido que lleva imagen
data class DetailItem(val title: String, val description: String, val imageResId: Int)

// 🚀 AHORA ES DINÁMICO: Mapa de Contenido Visual asociado a cada CategoryId
val dynamicDetailContent = mapOf(
    // 1: ATAQUE FEROZ
    1 to listOf(
        DetailItem(
            title = "Beyblade de Ataque Clave",
            description = "El diseño de la capa de energía agresiva es esencial. (noticia_resistencia)",
            imageResId = R.drawable.noticia_resistencia
        ),
        DetailItem(
            title = "Driver de Ataque (Plano)",
            description = "Esta punta maximiza el movimiento periférico. (noticia_balance)",
            imageResId = R.drawable.noticia_balance
        )
    ),

    // 2: DEFENSA INFRANQUEABLE
    2 to listOf(
        DetailItem(
            title = "Beyblade de Defensa de Perfil Bajo",
            description = "Distribución de peso centrada para máxima estabilidad. (noticia_balance)",
            imageResId = R.drawable.noticia_balance
        ),
        DetailItem(
            title = "Driver de Defensa (Esférico)",
            description = "La punta redonda absorbe impactos y minimiza el KO. (noticia_prohibida)",
            imageResId = R.drawable.noticia_prohibida
        )
    ),

    // 3: RESISTENCIA INFINITA
    3 to listOf(
        DetailItem(
            title = "Beyblade de Resistencia Aerodinámico",
            description = "Diseño ligero para reducir la fricción con el aire. (noticia_resistencia)",
            imageResId = R.drawable.noticia_resistencia
        ),
        DetailItem(
            title = "Driver de Resistencia (Bearing)",
            description = "Usa rodamientos para reducir la fricción con el estadio. (noticia_prohibida)",
            imageResId = R.drawable.noticia_prohibida
        )
    ),

    // 4: BALANCE TOTAL
    4 to listOf(
        DetailItem(
            title = "Chasis de Balance Híbrido",
            description = "Una estructura que puede adaptarse a ataque o defensa. (noticia_balance)",
            imageResId = R.drawable.noticia_balance
        ),
        DetailItem(
            title = "Driver de Balance (Cambio de Modo)",
            description = "Puntas duales que cambian entre modo ataque y defensa. (noticia_resistencia)",
            imageResId = R.drawable.noticia_resistencia
        )
    ),

    // 5: CUSTOMIZACIÓN
    5 to listOf(
        DetailItem(
            title = "Discos de Forja Modificados",
            description = "Análisis de discos de metal pesado para mejorar el impacto. (noticia_prohibida)",
            imageResId = R.drawable.noticia_prohibida
        )
    )
)

// --- 2. COMPONENTE DE TARJETA DE DETALLE (Muestra la imagen y el texto) ---
@Composable
fun DetailContentCard(item: DetailItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Image(
                painter = painterResource(id = item.imageResId),
                contentDescription = item.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = item.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// --- 3. PANTALLA PRINCIPAL DE DETALLE ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavHostController, categoryId: Int?) {

    // Lógica de contenido dinámico (Triple: Título, Subtítulo, Contenido)
    val (titleText, subtitleText, bodyContent) = when (categoryId) {
        1 -> Triple(
            "Ataque Feroz 💥",
            "Estrategias: Ataque Feroz",
            "Los Beyblades de Ataque Feroz se especializan en la velocidad y el impacto. Su objetivo es golpear al Bey rival con la máxima potencia al comienzo de la batalla.\n\nConsejos Clave:\n1. Lanzamiento Potente: Utiliza un lanzamiento de alta velocidad en ángulo.\n2. Puntas Planas: Maximizan el movimiento en el borde del estadio.\n3. Riesgo: Deben terminar la batalla rápido o se agotan."
        )
        2 -> Triple(
            "Defensa Infranqueable 🛡️",
            "Estrategias de Muro Total",
            "Los Beyblades de Defensa se centran en la estabilidad y resistencia al impacto. Su objetivo es absorber los ataques y sobrevivir.\n\nConsejos Clave:\n1. Lanzamiento al Centro: Minimiza el movimiento y maximiza la estabilidad.\n2. Puntas de Bola o Anchas: Minimizan el retroceso.\n3. Peso y Perfil Bajo: Aumentan la estabilidad."
        )
        3 -> Triple(
            "Resistencia Infinita ⏳",
            "Estrategias de Máxima Duración",
            "Diseñados para minimizar la pérdida de energía por fricción. Su estrategia es la supervivencia: dejar que los Bley de Ataque se agoten primero.\n\nConsejos Clave:\n1. Discos de Energía Ligeros: Reducen la resistencia al aire.\n2. Puntas de Giro Libre: Minimizan el contacto con el estadio.\n3. Lanzamiento Suave y Vertical: Conserva la energía al inicio."
        )
        4 -> Triple(
            "Balance Total ⚖️",
            "La Versatilidad Estratégica",
            "Los más versátiles, combinando elementos de Ataque, Defensa y Resistencia para adaptarse al oponente.\n\nConsejos Clave:\n1. Discos Híbridos: Equilibrio en la distribución del peso.\n2. Puntas Mixtas: Mantienen la agresividad controlada.\n3. Lanzamiento Adaptable: Clave para leer al oponente."
        )
        5 -> Triple(
            "Customización ⚙️",
            "Piezas, Mods y Construcciones",
            "La customización implica seleccionar cuidadosamente el disco de energía, el anillo de forja y el driver/punta para una combinación superior.\n\nConsejos Clave:\n1. Dominio de Tipos: Decide si quieres Ataque, Defensa o Resistencia.\n2. Fricción y Movimiento: Máxima fricción para Ataque; mínima para Resistencia.\n3. Peso y Distribución: Crucial para todos los tipos."
        )
        else -> Triple(
            "Detalle de Categoría ${categoryId ?: "N/A"}",
            "Información General",
            "Contenido general para la categoría."
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(titleText) },
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
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Muestra el Subtítulo
            Text(
                text = subtitleText,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Divider()

            // 2. Muestra el Contenido Principal
            Text(
                text = bodyContent,
                style = MaterialTheme.typography.bodyLarge,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { navController.popBackStack() }, modifier = Modifier.fillMaxWidth()) {
                Text("Volver al Inicio")
            }

            // 🚀 SECCIÓN DE IMÁGENES DINÁMICAS
            Spacer(modifier = Modifier.height(24.dp))
            Divider()
            Text(
                text = "Ejemplos Visuales de Beyblades",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))

            // 🚨 Búsqueda dinámica del contenido visual basado en el categoryId
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
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

// --- 4. PREVIEW ---
@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    val mockNavController = rememberNavController()
    // Previsualiza la categoría de Ataque (ID 1)
    DetailScreen(navController = mockNavController, categoryId = 1)
}