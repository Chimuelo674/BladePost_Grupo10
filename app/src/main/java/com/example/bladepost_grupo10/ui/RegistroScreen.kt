package com.example.bladepost_grupo10.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(navController: NavHostController){
    val context = LocalContext.current

    var nombre by remember { mutableStateOf("") }
    var usuario by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }

    var isNameError by remember {mutableStateOf(false)}
    var isUserError by remember {mutableStateOf(false)}
    var isEmailError by remember {mutableStateOf(false)}
    var isPasswordError by remember {mutableStateOf(false)}

    val validaryregistrar:()->Unit ={
        isNameError = false
        isUserError = false
        isEmailError = false
        isPasswordError = false
        var hasError = false

        if(nombre.isEmpty()){
            isNameError = true
            hasError = true
        }
        if(usuario.isEmpty()){
            isUserError = true
            hasError = true
        }
        if(correo.isEmpty()|| !correo.contains("@")){
            isEmailError = true
            hasError = true
        }
        if(contrasena.length < 6){
                isPasswordError = true
                hasError = true
        }
        if (!hasError) {
            Toast.makeText(context, "Se a registrado correctamente", Toast.LENGTH_SHORT).show()
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registro de Usuario") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver a Login"
                        )
                    }
                }
            )
        }
    ){ paddingValues ->
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top

    ){
        Text(
            text = "Registro de Usuario",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(24.dp))

        //Campo para el nombre completo
        OutlinedTextField(
            value = nombre,
            onValueChange = {nombre = it; isNameError = false},
            label = {Text("Nombre completo")},
            modifier = Modifier.fillMaxWidth(),
            isError = isNameError,
            supportingText = {
                if(isNameError){
                Text(
                    text = "Porfavor ingrese su nombre completo",
                    color = MaterialTheme.colorScheme.error
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        //Campo del nombre del usuario
        OutlinedTextField(
            value = usuario,
            onValueChange = {usuario = it; isUserError = false},
            label = {Text("Nombre de Usuario")},
            modifier = Modifier.fillMaxWidth(),
            isError = isUserError,
            supportingText = {
                if(isUserError) Text(
                    text = "Porfavor ingrese su nombre de usuario correctamente",
                    color = MaterialTheme.colorScheme.error
                )
            }
        )

        Spacer(modifier = Modifier.height(12.dp))


        //Campo para el correo
        OutlinedTextField(
            value = correo,
            onValueChange = {correo = it; isEmailError = false},
            label = {Text("Correo electrónico")},
            modifier = Modifier.fillMaxWidth(),
            isError = isEmailError,
            supportingText = {
                if(isEmailError) Text(
                    text = "Correo Electornico invalido(ej:Alexis@ejemplo.com)",
                    color = MaterialTheme.colorScheme.error
                )
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        //Campo para la Contraseña
        OutlinedTextField(
            value = contrasena,
            onValueChange = {contrasena = it; isPasswordError = false},
            label = {Text("Contraseña")},
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            isError = isPasswordError,
            supportingText = {
                if(isPasswordError) Text(
                    text = "la contraseña debe ser maximo de 6 caracteres",
                    color = MaterialTheme.colorScheme.error
                )
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        //Boton
        Button(
            onClick = validaryregistrar,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrar")
            }
        }
    }
}