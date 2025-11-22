package com.example.bladepost_grupo10.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.bladepost_grupo10.R
// 🚀 NECESITAS ESTA IMPORTACIÓN (Asumiendo que Screens está en el paquete raíz)
import com.example.bladepost_grupo10.Screens
import com.example.bladepost_grupo10.data.UsuarioDao // CLAVE: Se requiere tu implementación de la DB


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavHostController) {
    val context = androidx.compose.ui.platform.LocalContext.current
    // ⚠️ ATENCIÓN: Esta línea requiere que la clase UsuarioDao esté implementada.
    val usuarioDao = remember {UsuarioDao(context)}

    // BLOQUES REMOVIDOS Y REEMPLAZADOS POR LA IMPORTACIÓN DE ARRIBA

    // 1. ESTADOS para almacenar los datos del formulario y el estado de la UI

    var emailOrUser by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loginMessage by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // Función de validación y navegación (lógica de prueba antigua, reemplazada por la lógica del DAO)
    val onLoginClick: () -> Unit = {
        if (emailOrUser.isEmpty() || password.isEmpty()) {
            loginMessage = "Por favor, ingresa tu correo y contraseña."
        } else if (emailOrUser == "usuario@test.com" && password == "1234") {
            loginMessage = "¡Inicio de sesión exitoso! 🎉"

            // USAMOS EL OBJETO SCREENS GLOBAL
            navController.navigate(Screens.HOME_SCREEN) {
                // Elimina todas las pantallas hasta el LOGIN_SCREEN, incluyéndolo.
                popUpTo(Screens.LOGIN_SCREEN) { inclusive = true }
            }

        } else {
            loginMessage = "Credenciales inválidas. Inténtalo de nuevo."
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Inicio de Sesión") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_nuevo),
                contentDescription = "Logo App",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .padding(vertical = 16.dp),
                contentScale = ContentScale.Fit,
                alignment = Alignment.Center
            )

            Text(
                text = "BladePost Login",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            //CAMPO DE CORREO / USUARIO
            OutlinedTextField(
                value = emailOrUser,
                onValueChange = { emailOrUser = it },
                label = { Text("Correo Electrónico o Usuario") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // CAMPO DE CONTRASEÑA
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = "Mostrar/Ocultar contraseña")
                    }
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // BOTÓN DE LOGIN
            Button(
                onClick = {
                    when {
                        emailOrUser.isEmpty() || password.isEmpty() -> {
                            Toast.makeText(context, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                        }
                        // Lógica principal usando el DAO
                        usuarioDao.verificarCredenciales(emailOrUser, password) -> {
                            Toast.makeText(context, "Inicio de sesion exitoso", Toast.LENGTH_SHORT).show()
                            navController.navigate(Screens.HOME_SCREEN){
                                popUpTo(Screens.LOGIN_SCREEN) {inclusive = true}
                            }
                        }
                        else -> {
                            Toast.makeText(context,"Credenciales invalidas", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = emailOrUser.isNotEmpty() && password.isNotEmpty()
            ) {
                Text("Iniciar Sesión")
            }

            // MENSAJE DE VALIDACIÓN
            if (loginMessage.isNotEmpty()) {
                Text(
                    text = loginMessage,
                    color = if (loginMessage.contains("exitoso")) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
            TextButton(
                onClick = {
                    navController.navigate(Screens.REGISTER_SCREEN)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("¿No tienes una cuenta? Registrate aquí")
            }
        }
    }
}

// Preview para ver el diseño en Android Studio
@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    MaterialTheme {
        LoginScreen(navController = rememberNavController())
    }
}