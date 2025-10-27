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

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun PerfilScreen(navController: NavHostController){
    val context = LocalContext.current



    var userName by remember{ mutableStateOf("Nombre de ejemplo") }
    var userEmail by remember() { mutableStateOf("correo de ejemplo@email.com")}
    var profileImageUri by remember { mutableStateOf<Uri?>(null) }
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
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(24.dp))


            Box(
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha= 0.2f))
                    .clickable{showImagePickerDialog = true},
                contentAlignment = Alignment.Center
            ){
                if(profileImageUri !=null) {
                    Image(
                        painter = rememberAsyncImagePainter(model = profileImageUri),
                        contentDescription = "Foto de Perfil",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }else{
                        Icon(
                            Icons.Filled.Edit,
                            contentDescription = "Editar foto de perfil",
                            modifier = Modifier.size(70.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "toca para cambiar la foto",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(32.dp))

                OutlinedTextField(
                    value = userName,
                    onValueChange = {userName = it},
                    label = { Text("Nombre del usuario")},
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = userEmail,
                    onValueChange = {userEmail = it},
                    label = { Text("Correo Electornico")},
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth()
                )
                if(showImagePickerDialog){
                    ImagePickerDialog(
                        onDismissRequest = {showImagePickerDialog = false},
                        onImageSelected = {uri ->
                            profileImageUri = uri
                            showImagePickerDialog = false
                        }
                    )
                }
            }
        }


    }

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