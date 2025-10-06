package com.example.bladepost_grupo10.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp


@Composable
fun RegistroScreen(){
    val context = LocalContext.current

    var nombre by remember { mutableStateOf("") }
    var usuario by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = "Registro de Usuario - BlaydePost",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(24.dp))

        //Campo para el nombre completo
        OutlinedTextField(
            value = nombre,
            onValueChange = {nombre = it},
            label = {Text("Nombre completo")},
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        //Campo del nombre del usuario
        OutlinedTextField(
            value = usuario,
            onValueChange = {usuario = it},
            label = {Text("Nombre de Usuario")},
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))


        //Campo para el correo
        OutlinedTextField(
            value = correo,
            onValueChange = {correo = it},
            label = {Text("Correo electrónico")},
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        //Campo para la Contraseña
        OutlinedTextField(
            value = contrasena,
            onValueChange = {contrasena = it},
            label = {Text("Contrasena")},
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        //Boton
        Button(
            onClick = {
                when{
                    nombre.isEmpty() -> Toast.makeText(context, "Ingresar tu nombre completo", Toast.LENGTH_SHORT).show()
                    usuario.isEmpty() -> Toast.makeText(context, "Ingresar tu nombre de usuario", Toast.LENGTH_SHORT).show()
                    correo.isEmpty() || !correo.contains("@")-> Toast.makeText(context, "Correo electrónico invalido", Toast.LENGTH_SHORT).show()
                    contrasena.length< 6 -> Toast.makeText(context, "La contraseña debe tener seis caracteres", Toast.LENGTH_SHORT).show()
                    else -> Toast.makeText(context, "Registro exitoso(datos listos para guardar)", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrar")
        }
    }
}