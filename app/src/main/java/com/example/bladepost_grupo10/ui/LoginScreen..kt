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
import com.example.bladepost_grupo10.Screens
import com.example.bladepost_grupo10.data.UsuarioDao


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavHostController) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val usuarioDao = remember {UsuarioDao(context)}


    var emailOrUser by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loginMessage by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }


    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(title = { Text("Inicio de Sesión") })
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp), // Padding interno de la tarjeta
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_nuevo),
                        contentDescription = "Logo App",
                        modifier = Modifier
                            .fillMaxWidth(0.6f) // Hace el logo más pequeño dentro de la tarjeta
                            .height(120.dp)
                            .padding(vertical = 8.dp),
                        contentScale = ContentScale.Fit,
                        alignment = Alignment.Center
                    )

                    Text(
                        text = "BladePost Login",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(bottom = 16.dp)
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