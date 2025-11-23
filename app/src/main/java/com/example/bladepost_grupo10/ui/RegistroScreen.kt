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
import com.example.bladepost_grupo10.data.UsuarioDao

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(navController: NavHostController){
    val context = LocalContext.current
    val usuarioDao = remember { UsuarioDao(context)}

    var nombre by remember { mutableStateOf("") }
    var usuario by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var confirmarContrasena by remember { mutableStateOf("") }

    var isNameError by remember {mutableStateOf(false)}
    var isUserError by remember {mutableStateOf(false)}
    var isEmailError by remember {mutableStateOf(false)}
    var isPasswordError by remember {mutableStateOf(false)}
    var isConfirmPasswordError by remember {mutableStateOf(false)}

    val nameRegex = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+\$".toRegex()
    val userRegex = "^[a-zA-Z0-9_]+\$".toRegex()
    val passwordRegex = "^[a-zA-Z0-9\\p{Punct}\\s\\S&&[^áéíóúÁÉÍÓÚñÑ]]+\$".toRegex()

    val validaryregistrar:()->Unit ={
        isNameError = false
        isUserError = false
        isEmailError = false
        isPasswordError = false
        isConfirmPasswordError = false
        var hasError = false

        // 1. VALIDACIÓN DE NOMBRE COMPLETO
        if(nombre.isEmpty() || !nombre.matches(nameRegex)){
            isNameError = true
            hasError = true
        }

        // 2. VALIDACIÓN DE USUARIO
        if(usuario.isEmpty() || !usuario.matches(userRegex)){
            isUserError = true
            hasError = true
        }

        // 3. VALIDACIÓN DE CORREO
        if(correo.isEmpty()|| !correo.contains("@")){
            isEmailError = true
            hasError = true
        }

        // 4. VALIDACIÓN DE CONTRASEÑA: Longitud Y Regex
        if(contrasena.length < 6 || !contrasena.matches(passwordRegex)){
            isPasswordError = true
            hasError = true
        }

        // 5. VALIDACIÓN DE CONFIRMACIÓN: Debe coincidir Y pasar la Regex
        if(confirmarContrasena.isEmpty()){
            isConfirmPasswordError = true
            hasError = true
        } else if (contrasena != confirmarContrasena){
            isConfirmPasswordError = true
            hasError = true
        } else if (!confirmarContrasena.matches(passwordRegex)){
            isConfirmPasswordError = true
            hasError = true
        }


        if (!hasError) {
            //verificar si el usuario o correo ya existen
            val existe = usuarioDao.verificarUsuarioExistente(usuario, correo)
            if (existe) {
                Toast.makeText(context, "El usuario o correo ya estan registrados", Toast.LENGTH_SHORT).show()
            } else {
                val exito = usuarioDao.registrarUsuario(usuario, correo, contrasena)
                if (exito) {
                    Toast.makeText(context, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show()
                    navController.popBackStack() //volver a la pantalla anterior(login)
                } else {
                    Toast.makeText(context, "Error al registrar usuario", Toast.LENGTH_SHORT).show()
                }
            }
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
                            text = "El nombre solo puede contener letras y espacios (sin números)",
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
                        text = "El usuario solo puede contener letras básicas, numeros y caracteres especiales (sin tildes, ñ o @)",
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
                onValueChange = {contrasena = it; isPasswordError = false; isConfirmPasswordError = false},
                label = {Text("Contraseña")},
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                isError = isPasswordError,
                supportingText = {
                    if(isPasswordError) Text(
                        text = "Debe tener al menos 6 caracteres y no puede contener letras en español (tildes o ñ)",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // NUEVO CAMPO: Confirmar Contraseña
            OutlinedTextField(
                value = confirmarContrasena,
                onValueChange = {confirmarContrasena = it; isConfirmPasswordError = false},
                label = {Text("Confirmar Contraseña")},
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                isError = isConfirmPasswordError,
                supportingText = {
                    if(isConfirmPasswordError) {
                        val message = when {
                            confirmarContrasena.isEmpty() -> "Por favor, confirme su contraseña."
                            !confirmarContrasena.matches(passwordRegex) -> "La contraseña no puede contener letras en español (tildes o ñ)."
                            else -> "Las contraseñas no coinciden."
                        }
                        Text(text = message, color = MaterialTheme.colorScheme.error)
                    }
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Boton
            Button(
                onClick = validaryregistrar,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrar")
            }
        }
    }
}