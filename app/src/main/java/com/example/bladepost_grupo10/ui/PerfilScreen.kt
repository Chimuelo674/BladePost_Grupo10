@file:OptIn(ExperimentalPermissionsApi::class)

package com.example.bladepost_grupo10.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.bladepost_grupo10.utils.getNewImageFile
import com.example.bladepost_grupo10.utils.getUriForFile
import com.example.bladepost_grupo10.Screens
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import android.Manifest
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
// 🚀 IMPORTACIONES ADICIONALES PARA PERSISTENCIA
import androidx.core.net.toUri
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import android.content.Context
// ----------------------------------------

// --- FUNCIÓN AUXILIAR PARA PERSISTIR LA IMAGEN ---
/**
 * Copia el contenido de una URI de origen (temporal) a un archivo permanente
 * en el almacenamiento interno de la aplicación y devuelve la nueva URI.
 */
private fun persistImageUri(context: Context, sourceUri: Uri): Uri? {
    // Archivo de destino permanente
    val destFile = File(context.filesDir, "profile_pic_persisted.jpg")
    try {
        // Abre el InputStream de la URI de origen (cámara o galería)
        context.contentResolver.openInputStream(sourceUri)?.use { inputStream ->
            // Abre el OutputStream del archivo de destino
            FileOutputStream(destFile).use { outputStream ->
                // Copia los bytes
                inputStream.copyTo(outputStream)
            }
        }
        // Devuelve la URI del archivo guardado permanentemente
        return destFile.toUri()
    } catch (e: Exception) {
        e.printStackTrace()
        // Si falla, devuelve null
        return null
    }
}
// ------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(navController: NavHostController){
    val context = LocalContext.current

    // 🚀 ESTADOS PARA LA EDICIÓN Y PERSISTENCIA DE FOTO
    // Se cargan valores iniciales o simulados
    var userName by remember{ mutableStateOf("Nombre de ejemplo") }
    var userEmail by remember() { mutableStateOf("correo de ejemplo@email.com")}

    // 🚀 Cargar la foto guardada al iniciar
    var profileImageUri by remember {
        mutableStateOf(
            File(context.filesDir, "profile_pic_persisted.jpg")
                .takeIf { it.exists() }?.toUri()
        )
    }
    var showImagePickerDialog by remember {mutableStateOf(false)}

    Scaffold(
        topBar ={
            TopAppBar(
                title = { Text("Mi perfil") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack()}) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "volver")
                    }
                }
            )
        }
    ){paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // SECCIÓN DE FOTO DE PERFIL CON BOTÓN DE EDICIÓN
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha= 0.2f)),
                contentAlignment = Alignment.Center
            ){
                if(profileImageUri !=null) {
                    Image(
                        painter = rememberAsyncImagePainter(model = profileImageUri),
                        contentDescription = "Foto de Perfil",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        Icons.Filled.Person,
                        contentDescription = "Placeholder de foto",
                        modifier = Modifier.size(100.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Botón de Edición (Icono Edit)
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Editar foto",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .align(Alignment.BottomEnd)
                        .clickable { showImagePickerDialog = true }
                        .padding(8.dp),
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 🚀 CAMPOS EDITABLES
            OutlinedTextField(
                value = userName,
                onValueChange = {userName = it},
                label = { Text("Nombre del usuario")},
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = userEmail,
                onValueChange = {userEmail = it},
                label = { Text("Correo Electrónico")},
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            // 🚀 BOTÓN DE GUARDAR CAMBIOS
            Button(
                onClick = {
                    // 💡 Lógica para guardar userName y userEmail en la BD
                    println("Guardando: User=$userName, Email=$userEmail")
                },
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("Guardar Cambios de Perfil")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de Cerrar Sesión (simulado)
            Button(
                onClick = { navController.navigate(Screens.LOGIN_SCREEN) { popUpTo(Screens.HOME_SCREEN) { inclusive = true } } },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Cerrar Sesión")
            }

            //🚀 LLAMADA AL DIÁLOGO CON PERSISTENCIA
            if(showImagePickerDialog){
                ImagePickerDialog(
                    onDismissRequest = {showImagePickerDialog = false},
                    onImageSelected = { uri ->
                        // ✅ Persiste la nueva URI antes de asignarla a profileImageUri
                        profileImageUri = persistImageUri(context, uri)
                        showImagePickerDialog = false
                    }
                )
            }
        }
    }
}

// ----------------------------------------------------------------------
//ImagePickerDialog - DEFINIDO GLOBALMENTE PARA QUE POSTFORMSCREEN PUEDA USARLO
// ----------------------------------------------------------------------
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ImagePickerDialog(
    onDismissRequest:() -> Unit,
    onImageSelected:(Uri) -> Unit
){
    val context = LocalContext.current

    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    var tempPhotoUri  by remember { mutableStateOf<Uri?>(null) }


    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->

            if (success) {
                tempPhotoUri?.let { onImageSelected(it) }
            }
            onDismissRequest()
            tempPhotoUri = null

        }
    )
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {uri : Uri? ->
            uri?.let { onImageSelected(it) }
            onDismissRequest()
        }
    )
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {Text("Seleccionar Imagen")},
        text = {
            Column {
                TextButton(onClick = {
                    when{
                        cameraPermissionState.status.isGranted ->{

                            val photoFile = getNewImageFile(context)
                            val photoUri: Uri = getUriForFile(context,photoFile)
                            tempPhotoUri = photoUri
                            takePictureLauncher.launch(photoUri)
                        }
                        cameraPermissionState.status.shouldShowRationale ->{
                            cameraPermissionState.launchPermissionRequest()
                        }else ->{
                        cameraPermissionState.launchPermissionRequest()
                    }
                    }

                }) {
                    Icon(Icons.Filled.CameraAlt, contentDescription = "Tomar Foto")
                    Spacer(Modifier.width(8.dp))
                    Text("Tomar Foto")
                }
                TextButton(onClick = { pickImageLauncher.launch("image/*") }) {
                    Icon(Icons.Filled.PhotoLibrary, contentDescription = "Seleccionar de galería")
                    Spacer(Modifier.width(8.dp))
                    Text("Selecciona foto de la galeria")
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismissRequest) {
                Text("cancelar")
            }
        }
    )
}