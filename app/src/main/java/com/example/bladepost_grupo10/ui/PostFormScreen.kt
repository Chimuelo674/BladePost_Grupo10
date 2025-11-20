package com.example.bladepost_grupo10.ui

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostFormScreen(navController: NavHostController) {
    var questionText by remember { mutableStateOf("") }
    var titleText by remember { mutableStateOf("") }

    // 🚀 ESTADOS PARA LA FOTO
    var attachedImageUri by remember { mutableStateOf<Uri?>(null) }
    var showImagePickerDialog by remember { mutableStateOf(false) }

    // HARDCODED USERNAME: Reemplazar con el nombre real del usuario logeado
    val currentUserName = "Usuario_Autenticado_123"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Nueva Pregunta") },
                navigationIcon = {
                    // Botón para volver (regresa a ForumScreen)
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->

        //🚀 LLAMADA AL DIÁLOGO
        if (showImagePickerDialog) {
            // Se usa el ImagePickerDialog definido en PerfilScreen.kt
            ImagePickerDialog(
                onDismissRequest = { showImagePickerDialog = false },
                onImageSelected = { uri ->
                    attachedImageUri = uri
                    showImagePickerDialog = false
                }
            )
        }

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top)
        ) {

            Text(
                text = "Publicando como: ${currentUserName}",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth()
            )

            HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)

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
                    .height(150.dp)
            )

            // 🚀 BOTÓN Y VISTA PREVIA DE FOTO
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { showImagePickerDialog = true },
                    contentPadding = if (attachedImageUri != null) PaddingValues(8.dp) else ButtonDefaults.ContentPadding
                ) {
                    Icon(
                        Icons.Filled.AddAPhoto,
                        contentDescription = "Adjuntar Foto"
                    )
                    if (attachedImageUri == null) {
                        Spacer(Modifier.width(8.dp))
                        Text("Adjuntar Foto")
                    }
                }

                Spacer(Modifier.width(16.dp))

                if (attachedImageUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(model = attachedImageUri),
                        contentDescription = "Imagen adjunta",
                        modifier = Modifier
                            .size(64.dp)
                            .clip(MaterialTheme.shapes.small),
                        contentScale = ContentScale.Crop
                    )
                }
            }


            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    // Lógica para enviar la pregunta
                    println("Pregunta enviada: Título='${titleText}' por el usuario '${currentUserName}', Foto adjunta: ${attachedImageUri != null}")
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