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
import androidx.core.net.toUri
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import android.content.Context
import kotlinx.coroutines.launch
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.runtime.saveable.rememberSaveable

// --- FUNCIÓN AUXILIAR PARA PERSISTIR LA IMAGEN (Acepta userId) ---
/**
 * Copia el contenido de una URI de origen a un archivo permanente con un nombre único basado en el userId.
 */
private fun persistImageUri(context: Context, sourceUri: Uri, userId: String): Uri? {
    // 💡 SOLUCIÓN: Usar el userId en el nombre del archivo
    val destFile = File(context.filesDir, "profile_pic_user_${userId}.jpg")
    try {
        context.contentResolver.openInputStream(sourceUri)?.use { inputStream ->
            FileOutputStream(destFile).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        return destFile.toUri()
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}
// ------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(navController: NavHostController){
    val context = LocalContext.current

    // 💡 SIMULACIÓN DE ID DE USUARIO: En una app real, este ID vendría de Firebase Auth (e.g., auth.currentUser?.uid)
    // Usamos rememberSaveable para mantener el ID simulado
    val currentUserId = rememberSaveable { mutableStateOf("user_A_123") }.value // Mock ID fijo para A
    // Si quisieras simular que el usuario cambia, tendrías que cambiar este estado en el login/logout

    // 1. ESTADOS PARA EL SNACKBAR
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // ESTADOS PARA LA EDICIÓN Y PERSISTENCIA DE FOTO
    var userName by remember{ mutableStateOf("Tyson Granger") }
    var userEmail by remember() { mutableStateOf("tyson.granger@wbba.org")}

    // Cargar la foto guardada al iniciar
    // 💡 SOLUCIÓN: Buscar el archivo con el nombre único del usuario actual
    var profileImageUri by remember {
        mutableStateOf(
            File(context.filesDir, "profile_pic_user_${currentUserId}.jpg")
                .takeIf { it.exists() }?.toUri()
        )
    }
    var showImagePickerDialog by remember {mutableStateOf(false)}

    // 2. FUNCIÓN CENTRALIZADA PARA PERSISTENCIA Y FEEDBACK
    val handleImagePersistence: (Uri) -> Unit = { uri ->
        // 💡 SOLUCIÓN: Pasar el userId a la función de guardado
        val newUri = persistImageUri(context, uri, currentUserId)
        profileImageUri = newUri
        showImagePickerDialog = false

        scope.launch {
            if (newUri != null) {
                snackbarHostState.showSnackbar(
                    message = "Foto de perfil guardada exitosamente.",
                    duration = SnackbarDuration.Short
                )
            } else {
                snackbarHostState.showSnackbar(
                    message = "Error al guardar la imagen.",
                    duration = SnackbarDuration.Short
                )
            }
        }
    }

    Scaffold(
        // 3. AÑADIR EL HOST DEL SNACKBAR
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar ={
            TopAppBar(
                title = { Text("Mi Perfil - ID: ${currentUserId}") }, // Mostrar ID simulado para debug
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack()}) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ){paddingValues ->
        // Contenedor principal con Scroll y relleno
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(24.dp), // Relleno exterior para la tarjeta
            contentAlignment = Alignment.TopCenter
        ) {
            // INICIO DE LA TARJETA BLANCA
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter), // Asegura que la tarjeta esté centrada si no llena el ancho
                shape = MaterialTheme.shapes.large,
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp), // Relleno interior para los elementos de la tarjeta
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {

                    // SECCIÓN DE FOTO DE PERFIL
                    Box(
                        modifier = Modifier
                            .size(150.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha= 0.2f))
                            .clickable { showImagePickerDialog = true },
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

                        // Botón de Edición
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Editar foto",
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary)
                                .align(Alignment.BottomEnd)
                                .padding(8.dp),
                            tint = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // CAMPOS EDITABLES
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

                    // BOTÓN DE GUARDAR CAMBIOS DE PERFIL
                    Button(
                        onClick = {
                            // Simular la persistencia y mostrar feedback
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "Cambios de perfil guardados.",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(50.dp)
                    ) {
                        Text("Guardar Cambios de Perfil")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Botón de Cerrar Sesión (simulado)
                    Button(
                        onClick = {
                            // 💡 Nota: Al simular el cierre de sesión, en un caso real se cambiaría currentUserId
                            navController.navigate(Screens.LOGIN_SCREEN) { popUpTo(Screens.HOME_SCREEN) { inclusive = true } }
                        },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Cerrar Sesión")
                    }
                }
            } // FIN DE LA TARJETA BLANCA

            // LLAMADA AL DIÁLOGO CON PERSISTENCIA
            if(showImagePickerDialog){
                ImagePickerDialog(
                    onDismissRequest = {showImagePickerDialog = false},
                    onImageSelected = handleImagePersistence // Usa la función con Snackbar
                )
            }
        }
    }
}

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

        }
    )
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {uri : Uri? ->
            uri?.let { onImageSelected(it) }
            // onImageSelected ya maneja el cierre de showImagePickerDialog = false
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
                Text("Cancelar")
            }
        }
    )
}